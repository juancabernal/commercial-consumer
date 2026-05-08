package com.eatup.commercial.messaging.inventory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InventoryMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.inventory}")
    private String inventoryExchange;

    @Value("${rabbitmq.routing-key.inventory.stock}")
    private String stockRoutingKey;

    public InventoryMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishStockUpdate(StockUpdateMessage message) {
        rabbitTemplate.convertAndSend(inventoryExchange, stockRoutingKey, message);
    }
}