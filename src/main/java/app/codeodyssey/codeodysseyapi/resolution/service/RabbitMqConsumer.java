package app.codeodyssey.codeodysseyapi.resolution.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.consumer.queue.name}"})
    public void consumer(String message) {
        LOGGER.info(String.format("Received message -> %s", message));
    }
}