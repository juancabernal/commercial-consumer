package com.eatup.commercial.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommercialRabbitMQConfig {

    @Value("${rabbitmq.exchange.commercial}")
    private String exchangeName;

    @Value("${rabbitmq.queue.table-crud}")
    private String tableCrudQueueName;

    @Value("${rabbitmq.routing-key.table-crud}")
    private String tableCrudRoutingKey;

    @Value("${rabbitmq.queue.table-session}")
    private String tableSessionQueueName;

    @Value("${rabbitmq.routing-key.table-session}")
    private String tableSessionRoutingKey;

    @Value("${rabbitmq.queue.table-reservation}")
    private String tableReservationQueueName;

    @Value("${rabbitmq.routing-key.table-reservation}")
    private String tableReservationRoutingKey;


    @Value("${rabbitmq.exchange.table-session-open}")
    private String tableSessionOpenExchangeName;

    @Value("${rabbitmq.queue.table-session-open}")
    private String tableSessionOpenQueueName;

    @Value("${rabbitmq.routing-key.table-session-open}")
    private String tableSessionOpenRoutingKey;

    @Value("${rabbitmq.queue.purchase}")
    private String purchaseQueue;

    @Value("${rabbitmq.routing-key.purchase}")
    private String purchaseRoutingKey;

    @Value("${rabbitmq.exchange.dlx}")
    private String deadLetterExchangeName;

    @Value("${rabbitmq.queue.purchase.dlq}")
    private String purchaseDeadLetterQueue;

    @Value("${rabbitmq.routing-key.purchase.dlq}")
    private String purchaseDeadLetterRoutingKey;

    @Value("${rabbitmq.queue.seller}")
    private String sellerQueueName;

    @Value("${rabbitmq.routing-key.seller}")
    private String sellerRoutingKey;

    @Value("${rabbitmq.queue.discount}")
    private String discountQueueName;

    @Value("${rabbitmq.routing-key.discount}")
    private String discountRoutingKey;

    @Value("${rabbitmq.queue.customer-discount}")
    private String customerDiscountQueueName;

    @Value("${rabbitmq.routing-key.customer-discount}")
    private String customerDiscountRoutingKey;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.initialize();
        return admin;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rawRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public DirectExchange commercialExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue tableCrudQueue() {
        return QueueBuilder.durable(tableCrudQueueName).build();
    }

    @Bean
    public Binding tableCrudBinding(Queue tableCrudQueue, DirectExchange commercialExchange) {
        return BindingBuilder.bind(tableCrudQueue).to(commercialExchange).with(tableCrudRoutingKey);
    }

    @Bean
    public Queue tableSessionQueue() {
        return QueueBuilder.durable(tableSessionQueueName).build();
    }

    @Bean
    public Binding tableSessionBinding(Queue tableSessionQueue, DirectExchange commercialExchange) {
        return BindingBuilder.bind(tableSessionQueue).to(commercialExchange).with(tableSessionRoutingKey);
    }

    @Bean
    public Queue tableReservationQueue() {
        return QueueBuilder.durable(tableReservationQueueName).build();
    }

    @Bean
    public Binding tableReservationBinding(Queue tableReservationQueue, DirectExchange commercialExchange) {
        return BindingBuilder.bind(tableReservationQueue).to(commercialExchange).with(tableReservationRoutingKey);
    }

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

    @Bean
    public Queue salesCreateResponseQueue(
            org.springframework.core.env.Environment environment) {
        return new Queue(environment.getProperty("sales.create.response.queue"), true);
    }


    @Bean
    public DirectExchange tableSessionOpenExchange() {
        return new DirectExchange(tableSessionOpenExchangeName);
    }

    @Bean
    public Queue tableSessionOpenQueue() {
        return QueueBuilder.durable(tableSessionOpenQueueName).build();
    }

    @Bean
    public Binding tableSessionOpenBinding(Queue tableSessionOpenQueue, DirectExchange tableSessionOpenExchange) {
        return BindingBuilder.bind(tableSessionOpenQueue).to(tableSessionOpenExchange).with(tableSessionOpenRoutingKey);
    }

    @Bean
    public Queue purchaseQueue() {
        return QueueBuilder
                .durable(purchaseQueue)
                .deadLetterExchange(deadLetterExchangeName)
                .deadLetterRoutingKey(purchaseDeadLetterRoutingKey)
                .build();
    }

    @Bean
    public Binding purchaseBinding(
            Queue purchaseQueue,
            DirectExchange commercialExchange) {
        return BindingBuilder
                .bind(purchaseQueue)
                .to(commercialExchange)
                .with(purchaseRoutingKey);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchangeName);
    }

    @Bean
    public Queue purchaseDeadLetterQueue() {
        return QueueBuilder
                .durable(purchaseDeadLetterQueue)
                .build();
    }

    @Bean
    public Binding purchaseDeadLetterBinding(
            Queue purchaseDeadLetterQueue,
            DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(purchaseDeadLetterQueue)
                .to(deadLetterExchange)
                .with(purchaseDeadLetterRoutingKey);
    }

    @Bean
    public Queue sellerQueue() {
        return QueueBuilder.durable(sellerQueueName).build();
    }

    @Bean
    public Binding sellerBinding(Queue sellerQueue, DirectExchange commercialExchange) {
        return BindingBuilder
                .bind(sellerQueue)
                .to(commercialExchange)
                .with(sellerRoutingKey);
    }

    @Bean
    public Queue discountQueue() {
        return QueueBuilder.durable(discountQueueName).build();
    }

    @Bean
    public Binding discountBinding(Queue discountQueue, DirectExchange commercialExchange) {
        return BindingBuilder.bind(discountQueue).to(commercialExchange).with(discountRoutingKey);
    }

    @Bean
    public Queue customerDiscountQueue() {
        return QueueBuilder.durable(customerDiscountQueueName).build();
    }

    @Bean
    public Binding customerDiscountBinding(Queue customerDiscountQueue, DirectExchange commercialExchange) {
        return BindingBuilder.bind(customerDiscountQueue).to(commercialExchange).with(customerDiscountRoutingKey);
    }
}