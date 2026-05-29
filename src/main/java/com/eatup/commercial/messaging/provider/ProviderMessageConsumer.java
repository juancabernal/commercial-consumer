package com.eatup.commercial.messaging.provider;

import com.eatup.commercial.service.provider.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
            providerService.createProvider(event.getPayload());
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
            providerService.updateProvider(event.getProviderId(), event.getPayload());
        } catch (Exception e) {
            log.error("Error procesando actualización de proveedor. id={}, error={}",
                    event != null ? event.getProviderId() : "null", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.provider-status}")
    public void consumeStatus(ProviderCommandEvent event) {
        try {
            if (event == null || event.getProviderId() == null || event.getStatusPayload() == null) {
                log.error("Evento de estado de proveedor nulo o incompleto");
                return;
            }
            Object statusValue = event.getStatusPayload().get("status");
            if (statusValue == null) {
                log.error("Campo 'status' no encontrado en el payload del evento");
                return;
            }
            providerService.updateStatus(event.getProviderId(), statusValue.toString());
        } catch (Exception e) {
            log.error("Error procesando cambio de estado de proveedor. id={}, error={}",
                    event != null ? event.getProviderId() : "null", e.getMessage(), e);
        }
    }
}