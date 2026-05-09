package com.eatup.commercial.messaging.discount;

import com.eatup.commercial.dto.discount.DiscountDTO;
import com.eatup.commercial.service.discount.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
public class DiscountMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(DiscountMessageConsumer.class);

    private final DiscountService discountService;

    public DiscountMessageConsumer(DiscountService discountService) {
        this.discountService = discountService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.discount}")
    public void consume(DiscountCommandEvent event) {
        switch (event.getEventType()) {
            case "DISCOUNT_CREATED"        -> discountService.createDiscount(toDto(event.getPayload()));
            case "DISCOUNT_UPDATED"        -> discountService.updateDiscount(toUUID(event.getDiscountId()), toDto(event.getPayload()));
            case "DISCOUNT_STATUS_UPDATED" -> discountService.updateDiscountStatus(toUUID(event.getDiscountId()), toBoolean(value(event.getPayload(), "status")));
            case "DISCOUNT_DELETED"        -> discountService.deleteDiscount(toUUID(event.getDiscountId()));
            default -> log.warn("Evento de descuento no reconocido: {}", event.getEventType());
        }
    }

    private DiscountDTO toDto(Map<String, Object> payload) {
        DiscountDTO dto = new DiscountDTO();
        dto.setCategoryId(toUUID(value(payload, "categoryId")));
        dto.setPercentage(toInteger(value(payload, "percentage")));
        dto.setDescription(toString(value(payload, "description")));
        dto.setStatus(toBoolean(value(payload, "status")));
        return dto;
    }

    private Object value(Map<String, Object> payload, String key) { return payload != null ? payload.get(key) : null; }
    private UUID toUUID(Object v) { return v != null ? UUID.fromString(v.toString()) : null; }
    private Integer toInteger(Object v) { return v instanceof Number n ? n.intValue() : v != null ? Integer.valueOf(v.toString()) : null; }
    private Boolean toBoolean(Object v) { return v instanceof Boolean b ? b : v != null ? Boolean.valueOf(v.toString()) : null; }
    private String toString(Object v) { return v != null ? v.toString() : null; }
}