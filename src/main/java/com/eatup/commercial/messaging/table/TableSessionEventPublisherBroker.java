package com.eatup.commercial.messaging.table;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableSessionEventPublisherBroker implements TableSessionEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.table-session-open}")
    private String tableSessionOpenExchange;

    @Value("${rabbitmq.routing-key.table-session-open}")
    private String tableSessionOpenRoutingKey;

    @Override
    public void publishOpenSessionRequested(TableSessionOpenRequestedMessage message) {
        MessagePostProcessor postProcessor = rabbitMessage -> {
            rabbitMessage.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
            rabbitMessage.getMessageProperties().setHeader("idMensaje", UUID.randomUUID().toString());
            return rabbitMessage;
        };

        log.info("Publishing table session open request. tableId={}, saleId={}", message.getTableId(), message.getSaleId());
        rabbitTemplate.convertAndSend(tableSessionOpenExchange, tableSessionOpenRoutingKey, message, postProcessor);
        log.info("Table session open request published. tableId={}, saleId={}", message.getTableId(), message.getSaleId());
    }
}
