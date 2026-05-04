package com.eatup.commercial.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommercialRabbitMQConfig {

    @Bean
    public DirectExchange salesPatchRequestExchange(
            org.springframework.core.env.Environment environment) {
        return new DirectExchange(environment.getProperty("sales.patch.request.exchange"));
    }

    @Bean
    public Queue salesPatchRequestQueue(
            org.springframework.core.env.Environment environment) {
        return new Queue(environment.getProperty("sales.patch.request.queue"), true);
    }

    @Bean
    public Binding salesPatchRequestBinding(
            Queue salesPatchRequestQueue,
            DirectExchange salesPatchRequestExchange,
            org.springframework.core.env.Environment environment) {
        return BindingBuilder.bind(salesPatchRequestQueue)
                .to(salesPatchRequestExchange)
                .with(environment.getProperty("sales.patch.request.routingKey"));
    }
}
