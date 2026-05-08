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
public class SaleDeleteConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleDeleteConsumer.class);

    private final SaleService saleService;
    private final MapperJsonObjeto mapperJsonObjeto;

    public SaleDeleteConsumer(SaleService saleService, MapperJsonObjeto mapperJsonObjeto) {
        this.saleService = saleService;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

    @RabbitListener(
            queues = "${sales.delete.response.queue}",
            containerFactory = "rawRabbitListenerContainerFactory"
    )
    public void handleSaleDelete(Message rabbitMessage) {
        String message = new String(rabbitMessage.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("Received sale delete response message: {}", message);

        try {
            Optional<SaleDeleteResponseMessage> dtoOpt =
                    mapperJsonObjeto.ejecutar(message, SaleDeleteResponseMessage.class);

            if (dtoOpt.isEmpty()) {
                LOGGER.error("Could not deserialize sale delete response message. Raw message: {}", message);
                return;
            }

            SaleDeleteResponseMessage dto = dtoOpt.get();
            saleService.applyDeleteResponse(dto);
            LOGGER.info("Sale delete response processed successfully. saleId={}", dto.getSaleId());

        } catch (Exception e) {
            LOGGER.error(
                    "Failed to process sale delete response message. Raw message: {}. Error: {}",
                    message,
                    e.getMessage(),
                    e
            );
        }
    }
}
