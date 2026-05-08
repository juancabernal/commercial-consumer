package com.eatup.commercial.messaging.seller;

import com.eatup.commercial.domain.seller.SellerStatus;
import com.eatup.commercial.dto.seller.SellerDTO;
import com.eatup.commercial.dto.seller.SellerPatchDTO;
import com.eatup.commercial.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SellerMessageConsumer {

    private static final String STATUS = "status";

    private final SellerService sellerService;

    @RabbitListener(queues = "${rabbitmq.queue.seller}")
    public void consume(SellerCommandEvent event) {
        switch (event.getEventType()) {
            case "SELLER_CREATED" ->
                    sellerService.createSeller(toSellerDto(event.getPayload()));

            case "SELLER_UPDATED" ->
                    sellerService.updateSeller(event.getSellerId(), toSellerDto(event.getPayload()));

            case "SELLER_STATUS_UPDATED" ->
                    sellerService.updateSellerStatus(
                            event.getSellerId(),
                            toString(value(event.getPayload(), STATUS))
                    );

            case "SELLER_PATCHED" ->
                    sellerService.patchSeller(event.getSellerId(), toSellerPatchDto(event.getPayload()));

            default ->
                    log.warn("Evento de seller no procesado por commercial-service: {}", event.getEventType());
        }
    }

    private SellerDTO toSellerDto(Map<String, Object> payload) {
        SellerDTO dto = new SellerDTO();

        dto.setId(toUUID(value(payload, "id")));
        dto.setDocumentTypeId(toUUID(value(payload, "documentTypeId")));
        dto.setLocationId(toUUID(value(payload, "locationId")));
        dto.setIdentificationNumber(toString(value(payload, "identificationNumber")));
        dto.setFirstName(toString(value(payload, "firstName")));
        dto.setLastName(toString(value(payload, "lastName")));
        dto.setPhone(toString(value(payload, "phone")));
        dto.setEmail(toString(value(payload, "email")));
        dto.setCommissionPercentage(toDouble(value(payload, "commissionPercentage")));
        dto.setStatus(toSellerStatus(value(payload, STATUS)));

        return dto;
    }

    private SellerPatchDTO toSellerPatchDto(Map<String, Object> payload) {
        SellerPatchDTO dto = new SellerPatchDTO();

        dto.setFirstName(toString(value(payload, "firstName")));
        dto.setLastName(toString(value(payload, "lastName")));
        dto.setPhone(toString(value(payload, "phone")));
        dto.setCommissionPercentage(toDouble(value(payload, "commissionPercentage")));
        dto.setIdentificationNumber(toString(value(payload, "identificationNumber")));
        dto.setLocationId(toUUID(value(payload, "locationId")));
        dto.setDocumentTypeId(toUUID(value(payload, "documentTypeId")));

        return dto;
    }

    private Object value(Map<String, Object> payload, String key) {
        return payload != null ? payload.get(key) : null;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : null;
    }

    private UUID toUUID(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof UUID uuid) {
            return uuid;
        }

        return UUID.fromString(value.toString());
    }

    private Double toDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        return Double.valueOf(value.toString());
    }

    private SellerStatus toSellerStatus(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof SellerStatus sellerStatus) {
            return sellerStatus;
        }

        return SellerStatus.valueOf(value.toString().trim().toUpperCase());
    }
}