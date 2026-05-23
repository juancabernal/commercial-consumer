package com.eatup.commercial.messaging.provider;

import com.eatup.commercial.dto.provider.ProviderDTO;
import com.eatup.commercial.service.provider.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProviderMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProviderMessageConsumer.class);

    private final ProviderService providerService;

    public ProviderMessageConsumer(ProviderService providerService) {
        this.providerService = providerService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.provider-create}")
    public void consumeCreate(ProviderCommandEvent event) {
        try {
            if (event == null || event.getPayload() == null) {
                log.error("Evento de creación de proveedor nulo o sin payload");
                return;
            }
            providerService.createProvider(toDto(event.getPayload()));
        } catch (Exception e) {
            log.error("Error procesando creación de proveedor. error={}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.provider-update}")
    public void consumeUpdate(ProviderCommandEvent event) {
        try {
            if (event == null || event.getProviderId() == null || event.getPayload() == null) {
                log.error("Evento de actualización de proveedor nulo o incompleto");
                return;
            }
            providerService.updateProvider(event.getProviderId(), toDto(event.getPayload()));
        } catch (Exception e) {
            log.error("Error procesando actualización de proveedor. id={}, error={}",
                    event != null ? event.getProviderId() : "null", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.provider-status}")
    public void consumeStatus(ProviderCommandEvent event) {
        try {
            if (event == null || event.getProviderId() == null || event.getPayload() == null) {
                log.error("Evento de estado de proveedor nulo o incompleto");
                return;
            }
            String status = toString(value(event.getPayload(), "status"));
            providerService.updateStatus(event.getProviderId(), status);
        } catch (Exception e) {
            log.error("Error procesando cambio de estado de proveedor. id={}, error={}",
                    event != null ? event.getProviderId() : "null", e.getMessage(), e);
        }
    }

    private ProviderDTO toDto(Map<String, Object> payload) {
        ProviderDTO dto = new ProviderDTO();
        dto.setBusinessName(toString(value(payload, "businessName")));
        dto.setDocumentTypeId(toLong(value(payload, "documentTypeId")));
        dto.setDocumentNumber(toString(value(payload, "documentNumber")));
        dto.setTaxRegimeId(toLong(value(payload, "taxRegimeId")));
        dto.setResponsibleFirstName(toString(value(payload, "responsibleFirstName")));
        dto.setResponsibleLastName(toString(value(payload, "responsibleLastName")));
        dto.setPhone(toString(value(payload, "phone")));
        dto.setEmail(toString(value(payload, "email")));
        dto.setDepartmentId(toLong(value(payload, "departmentId")));
        dto.setCityId(toLong(value(payload, "cityId")));
        dto.setAddress(toString(value(payload, "address")));
        dto.setBranchId(toLong(value(payload, "branchId")));
        return dto;
    }

    private Object value(Map<String, Object> payload, String key) {
        return payload != null ? payload.get(key) : null;
    }

    private String toString(Object v) {
        return v != null ? v.toString() : null;
    }

    private Long toLong(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.longValue();
        try {
            return Long.parseLong(v.toString());
        } catch (NumberFormatException e) {
            log.error("Long inválido recibido en evento de proveedor: {}", v);
            return null;
        }
    }
}