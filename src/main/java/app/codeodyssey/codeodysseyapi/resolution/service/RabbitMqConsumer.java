package app.codeodyssey.codeodysseyapi.resolution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.consumer.queue.name}"})
    public void consumer(String message) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNodeMessage = objectMapper.readTree(message);
            String idResolution = jsonNodeMessage.get("id_resolution").asText();
            String resolutionTestResult = jsonNodeMessage.get("resolution_test_result").asText();
            System.out.println("idResolution: " + idResolution);
            System.out.println("Resolution Test Result: " + resolutionTestResult);
            JsonNode jsonNodeResult = objectMapper.readTree(resolutionTestResult);
            for (JsonNode jsonNode : jsonNodeResult) {
                String name = jsonNode.get("name").asText();
                String classname = jsonNode.get("classname").asText();
                String time = jsonNode.get("time").asText();
                String status = jsonNode.get("status").asText();

                // Faça o que quiser com os valores
                System.out.println("Name: " + name);
                System.out.println("Classname: " + classname);
                System.out.println("Time: " + time);
                System.out.println("Status: " + status);
                System.out.println();
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        LOGGER.info(String.format("Received message -> %s", message));
    }
}