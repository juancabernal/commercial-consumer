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
        try {
            if (event == null || event.getEventType() == null) {
                log.error("Evento de descuento nulo o sin tipo");
                return;
            }

            if ((event.getEventType().equals("DISCOUNT_CREATED") ||
                    event.getEventType().equals("DISCOUNT_UPDATED"))
                    && event.getPayload() == null) {
                log.error("Payload nulo para evento: {}", event.getEventType());
                return;
            }
            switch (event.getEventType()) {
                case "DISCOUNT_CREATED"        -> discountService.createDiscount(toDto(event.getPayload()));
                case "DISCOUNT_UPDATED"        -> discountService.updateDiscount(toUUID(event.getDiscountId()), toDto(event.getPayload()));
                case "DISCOUNT_STATUS_UPDATED" -> discountService.updateDiscountStatus(toUUID(event.getDiscountId()), toBoolean(value(event.getPayload(), "status")));
                case "DISCOUNT_DELETED"        -> discountService.deleteDiscount(toUUID(event.getDiscountId()));
                default -> log.warn("Evento de descuento no reconocido: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error procesando evento de descuento. tipo={}, id={}, error={}",
                    event != null ? event.getEventType() : "null",
                    event != null ? event.getDiscountId() : "null",
                    e.getMessage(), e);
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
    private UUID toUUID(Object v) {
        if (v == null) return null;
        try {
            return UUID.fromString(v.toString());
        } catch (IllegalArgumentException e) {
            log.error("UUID invalido recibido en evento de descuento: {}", v);
            return null;
        }
    }

    private Integer toInteger(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        try {
            return Integer.parseInt(v.toString());
        } catch (NumberFormatException e) {
            log.error("Integer invalido recibido en evento de descuento: {}", v);
            return null;
        }
    }

    private Boolean toBoolean(Object v) { return v instanceof Boolean b ? b : v != null ? Boolean.valueOf(v.toString()) : null; }
    private String toString(Object v) { return v != null ? v.toString() : null; }
}