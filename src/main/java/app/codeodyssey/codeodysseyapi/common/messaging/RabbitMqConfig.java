package app.codeodyssey.codeodysseyapi.common.messaging;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    private static final String PRODUCER_QUEUE = "execution_queue";
    private static final String CONSUMER_QUEUE = "result_queue";
    private static final String PRODUCER_EXCHANGE = "api_exchange";
    private static final String CONSUMER_EXCHANGE = "executor_exchange";
    @Value("${rabbitmq.producer.routing-key}")
    private String producerRoutingKey;
    @Value("${rabbitmq.consumer.routing-key}")
    private String consumerRoutingKey;
    @Value("${rabbitmq.ttl}")
    private String queueTtl;

    @Bean
    public Queue producerQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", Integer.valueOf(queueTtl));
        args.put("x-dead-letter-exchange", "execution_dlx");
        args.put("x-dead-letter-routing-key", "execution_dlq_key");
        return new Queue(PRODUCER_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue consumerQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", Integer.valueOf(queueTtl));
        args.put("x-dead-letter-exchange", "result_dlx");
        args.put("x-dead-letter-routing-key", "result_dlq_key");
        return new Queue(CONSUMER_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue producerDLQ() {
        return new Queue("execution_dlq");
    }

    @Bean
    public Queue consumerDLQ() {
        return new Queue("result_dlq");
    }

    @Bean
    public DirectExchange producerExchange() {
        return new DirectExchange(PRODUCER_EXCHANGE, false, false, null);
    }

    @Bean
    public DirectExchange consumerExchange() {
        return new DirectExchange(CONSUMER_EXCHANGE, false, false, null);
    }

    @Bean
    public DirectExchange producerDLX() {
        return new DirectExchange("execution_dlx", false, false, null);
    }

    @Bean
    public DirectExchange consumerDLX() {
        return new DirectExchange("result_dlx", false, false, null);
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

    @Bean
    public Binding producerBidingDLQ() {
        return BindingBuilder.bind(producerDLQ())
                .to(producerDLX())
                .with("execution_dlq_key");
    }

    @Bean
    public Binding consumerBidingDLQ() {
        return BindingBuilder.bind(consumerDLQ())
                .to(consumerDLX())
                .with("result_dlq_key");
    }
}