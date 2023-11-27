package app.codeodyssey.codeodysseyapi.result.service;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.result.data.Result;
import app.codeodyssey.codeodysseyapi.result.data.ResultRepository;
import app.codeodyssey.codeodysseyapi.result.data.TestCase;
import app.codeodyssey.codeodysseyapi.result.data.TestCaseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabbitMqConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumer.class);
    private final RabbitTemplate rabbitTemplate;
    private static final String DLX = "result_dlx";
    private static final String RESULT_QUEUE = "result_queue";

    private final ResultRepository resultRepository;
    private final TestCaseRepository testCaseRepository;
    private final ResolutionRepository resolutionRepository;

    @RabbitListener(queues = {RESULT_QUEUE})
    public void consumer(String message) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (message != null) {
                JsonNode jsonNode = objectMapper.readTree(message);
                String resolutionTestResult = jsonNode.get("result").asText();
                JsonNode jsonNodeMessage = objectMapper.readTree(resolutionTestResult);

                if (jsonNodeMessage.get("id") != null) {
                    String resultId = jsonNodeMessage.get("id").asText();
                    String resultName = jsonNodeMessage.get("name").asText();
                    String resultTime = jsonNodeMessage.get("time").asText();
                    String resolutionId = jsonNodeMessage.get("resolution_id").asText();
                    String resultError = jsonNodeMessage.get("error").asText();

                    Resolution resolution = resolutionRepository.findById(UUID.fromString(resolutionId))
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(UUID.fromString(resolutionId), Resource.RESOLUTION)
                            );

                    Result result = new Result(
                            UUID.fromString(resultId),
                            resultName,
                            Double.valueOf(resultTime),
                            resultError,
                            resolution
                    );

                    resultRepository.save(result);

                    JsonNode testCases = jsonNodeMessage.get("testCases");
                    if (testCases != null && testCases.isArray()) {
                        for (JsonNode j : testCases) {

                            TestCase testCase = new TestCase(
                                    UUID.fromString(j.get("id").asText()),
                                    j.get("testName").asText(),
                                    j.get("success").asBoolean(),
                                    j.get("info").asText(),
                                    Double.valueOf(j.get("time").asText()),
                                    result
                            );

                            testCaseRepository.save(testCase);

                        }
                    }
                } else {
                    rabbitTemplate.convertAndSend(DLX, "result_dlq_key", message);
                }
            } else {
                rabbitTemplate.convertAndSend(DLX, "result_dlq_key", message);
            }
        } catch (JsonProcessingException | AmqpException ex) {
            ex.printStackTrace();
        }
        LOGGER.info(String.format("Received message -> %s", message));
    }
}