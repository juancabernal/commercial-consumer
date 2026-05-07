package com.eatup.commercial.messaging.sales;

import com.eatup.commercial.service.sale.SaleService;
import com.eatup.commercial.util.json.MapperJsonObjeto;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SalePatchConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalePatchConsumer.class);

    private final SaleService saleService;
    private final MapperJsonObjeto mapperJsonObjeto;

    public SalePatchConsumer(SaleService saleService, MapperJsonObjeto mapperJsonObjeto) {
        this.saleService = saleService;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

    @RabbitListener(
            queues = "${sales.patch.request.queue}",
            containerFactory = "rawRabbitListenerContainerFactory"
    )
    public void handleSalePatch(Message rabbitMessage) {
        String message = new String(rabbitMessage.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("Received sale patch message: {}", message);

        try {
            Optional<SalePatchRequestedMessage> dtoOpt =
                    mapperJsonObjeto.ejecutar(message, SalePatchRequestedMessage.class);

            if (dtoOpt.isEmpty()) {
                LOGGER.error("Could not deserialize sale patch message. Raw message: {}", message);
                return;
            }

            SalePatchRequestedMessage dto = dtoOpt.get();
            saleService.applyPatch(dto);
            LOGGER.info("Sale patch message processed successfully. saleId={}", dto.getSaleId());
        } catch (Exception e) {
            LOGGER.error("Failed to process sale patch message. Raw message: {}. Error: {}",
                    message, e.getMessage(), e);
        }
    }
}
