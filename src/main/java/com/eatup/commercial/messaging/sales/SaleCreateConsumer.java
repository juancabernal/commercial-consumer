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
public class SaleCreateConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleCreateConsumer.class);

    private final SaleService saleService;
    private final MapperJsonObjeto mapperJsonObjeto;

    public SaleCreateConsumer(SaleService saleService, MapperJsonObjeto mapperJsonObjeto) {
        this.saleService = saleService;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

    @RabbitListener(
            queues = "${sales.create.response.queue}",
            containerFactory = "rawRabbitListenerContainerFactory"
    )
    public void handleSaleCreate(Message rabbitMessage) {
        String message = new String(rabbitMessage.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("Received sale create response message: {}", message);

        try {
            Optional<SaleCreateResponseMessage> dtoOpt =
                    mapperJsonObjeto.ejecutar(message, SaleCreateResponseMessage.class);

            if (dtoOpt.isEmpty()) {
                LOGGER.error("Could not deserialize sale create response message. Raw message: {}", message);
                return;
            }

            SaleCreateResponseMessage dto = dtoOpt.get();
            saleService.applyCreateResponse(dto);
            LOGGER.info("Sale create response processed successfully. idMessage={}", dto.getIdMessage());

        } catch (Exception e) {
            LOGGER.error("Failed to process sale create response message. Raw message: {}. Error: {}",
                    message, e.getMessage(), e);
        }
    }
}
