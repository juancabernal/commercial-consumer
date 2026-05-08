package com.eatup.commercial.messaging.customerDiscount;

import com.eatup.commercial.dto.customerDiscount.CustomerDiscountDTO;
import com.eatup.commercial.service.customerDiscount.CustomerDiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomerDiscountMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(CustomerDiscountMessageConsumer.class);

    private final CustomerDiscountService customerDiscountService;

    public CustomerDiscountMessageConsumer(CustomerDiscountService customerDiscountService) {
        this.customerDiscountService = customerDiscountService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.customer-discount}")
    public void consume(CustomerDiscountCommandEvent event) {
        switch (event.getEventType()) {
            case "CUSTOMER_DISCOUNT_CREATED" -> customerDiscountService.createCustomerDiscount(toDto(event.getPayload()));
            case "CUSTOMER_DISCOUNT_UPDATED" -> customerDiscountService.updateCustomerDiscount(toUUID(event.getCustomerDiscountId()), toDto(event.getPayload()));
            case "CUSTOMER_DISCOUNT_DELETED" -> customerDiscountService.deleteCustomerDiscount(toUUID(event.getCustomerDiscountId()));
            default -> log.warn("Evento de customer discount no reconocido: {}", event.getEventType());
        }
    }

    private CustomerDiscountDTO toDto(Map<String, Object> payload) {
        CustomerDiscountDTO dto = new CustomerDiscountDTO();
        dto.setCustomerId(toUUID(value(payload, "customerId")));
        dto.setLocationId(toUUID(value(payload, "locationId")));
        dto.setDiscountId(toUUID(value(payload, "discountId")));
        dto.setAssignedAt(toLocalDate(value(payload, "assignedAt")));
        dto.setStartDate(toLocalDate(value(payload, "startDate")));
        dto.setEndDate(toLocalDate(value(payload, "endDate")));
        return dto;
    }

    private Object value(Map<String, Object> payload, String key) { return payload != null ? payload.get(key) : null; }
    private UUID toUUID(Object v) { return v != null ? UUID.fromString(v.toString()) : null; }
    private LocalDate toLocalDate(Object v) {
        if (v == null) return null;
        if (v instanceof LocalDate d) return d;
        return LocalDate.parse(v.toString());
    }
}