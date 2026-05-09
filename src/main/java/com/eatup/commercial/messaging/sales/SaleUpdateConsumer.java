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
public class SaleUpdateConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleUpdateConsumer.class);

    private final SaleService saleService;
    private final MapperJsonObjeto mapperJsonObjeto;

    public SaleUpdateConsumer(SaleService saleService, MapperJsonObjeto mapperJsonObjeto) {
        this.saleService = saleService;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

    @RabbitListener(
            queues = "${sales.update.response.queue}",
            containerFactory = "rawRabbitListenerContainerFactory"
    )
    public void handleSaleUpdate(Message rabbitMessage) {
        String message = new String(rabbitMessage.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("Received sale update response message: {}", message);

        try {
            Optional<SaleUpdateResponseMessage> dtoOpt =
                    mapperJsonObjeto.ejecutar(message, SaleUpdateResponseMessage.class);

            if (dtoOpt.isEmpty()) {
                LOGGER.error("Could not deserialize sale update response message. Raw message: {}", message);
                return;
            }

            SaleUpdateResponseMessage dto = dtoOpt.get();
            saleService.applyUpdateResponse(dto);
            LOGGER.info("Sale update response processed successfully. saleId={}", dto.getSaleId());

        } catch (Exception e) {
            LOGGER.error(
                    "Failed to process sale update response message. Raw message: {}. Error: {}",
                    message,
                    e.getMessage(),
                    e
            );
        }
    }
}
