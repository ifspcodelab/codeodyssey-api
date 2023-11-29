package app.codeodyssey.codeodysseyapi.result.service;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionStatus;
import app.codeodyssey.codeodysseyapi.result.data.Result;
import app.codeodyssey.codeodysseyapi.result.data.ResultRepository;
import app.codeodyssey.codeodysseyapi.result.data.TestCase;
import app.codeodyssey.codeodysseyapi.result.data.TestCaseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqConsumer {
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
                JsonNode resolutionTestResult = jsonNode.get("result");

                if (resolutionTestResult.get("id") != null) {
                    String resultId = resolutionTestResult.get("id").asText();
                    String resultName = resolutionTestResult.get("name").asText();
                    String resultTime = resolutionTestResult.get("time").asText();
                    String resolutionId = resolutionTestResult.get("resolution_id").asText();
                    String resultError = resolutionTestResult.get("error").asText();

                    Resolution resolution = resolutionRepository.findById(UUID.fromString(resolutionId))
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(UUID.fromString(resolutionId), Resource.RESOLUTION)
                            );

                    if (resolutionTestResult.get("error").asText().equals("null")) {
                        resolution.setStatus(ResolutionStatus.EXECUTED_SUCCESS);
                    } else {
                        resolution.setStatus(ResolutionStatus.EXECUTED_ERROR);
                    }

                    resolutionRepository.save(resolution);

                    Result result = new Result(
                            UUID.fromString(resultId),
                            resultName,
                            Double.valueOf(resultTime),
                            resultError,
                            resolution
                    );

                    resultRepository.save(result);

                    JsonNode testCases = resolutionTestResult.get("testcases");
                    if (testCases != null && testCases.isArray()) {
                        for (JsonNode j : testCases) {

                            TestCase testCase = new TestCase(
                                    UUID.fromString(j.get("id").asText()),
                                    j.get("test_name").asText(),
                                    j.get("success").asBoolean(),
                                    j.get("info").asText(),
                                    Double.valueOf(j.get("time").asText()),
                                    result
                            );

                            testCaseRepository.save(testCase);

                        }
                    }
                    log.info("Message received: {}", message));
                } else {
                    log.warn("Message {} sent to dead letter queue", message);
                    rabbitTemplate.convertAndSend(DLX, "result_dlq_key", message);
                }
            } else {
                log.warn(" Null message sent to dead letter queue");
                rabbitTemplate.convertAndSend(DLX, "result_dlq_key", message);
            }
        } catch (JsonProcessingException | AmqpException ex) {
            log.error("Error when trying to process message from result queue", ex);
            ex.printStackTrace();
        }
    }
}