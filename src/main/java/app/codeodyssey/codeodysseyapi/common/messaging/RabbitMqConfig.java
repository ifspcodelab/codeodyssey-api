package app.codeodyssey.codeodysseyapi.common.messaging;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.producer.queue.name}")
    private String producerQueue;
    @Value("${rabbitmq.consumer.queue.name}")
    private String consumerQueue;
    @Value("${rabbitmq.producer.exchange.name}")
    private String producerExchange;
    @Value("${rabbitmq.consumer.exchange.name}")
    private String consumerExchange;
    @Value("${rabbitmq.producer.routing-key}")
    private String producerRoutingKey;
    @Value("${rabbitmq.consumer.routing-key}")
    private String consumerRoutingKey;

    @Bean
    public Queue producerQueue() {
        return new Queue(producerQueue);
    }

    @Bean
    public Queue consumerQueue() {
        return new Queue(consumerQueue);
    }

    @Bean
    public DirectExchange producerExchange() {
        return new DirectExchange(producerExchange, false, false, null);
    }

    @Bean
    public DirectExchange consumerExchange() {
        return new DirectExchange(consumerExchange, false, false, null);
    }

    @Bean
    public Binding producerBiding() {
        return BindingBuilder.bind(producerQueue())
                .to(producerExchange())
                .with(producerRoutingKey);
    }

    @Bean
    public Binding consumerBiding() {
        return BindingBuilder.bind(consumerQueue())
                .to(consumerExchange())
                .with(consumerRoutingKey);
    }
}