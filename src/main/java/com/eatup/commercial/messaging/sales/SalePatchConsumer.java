package com.eatup.commercial.messaging.sales;

import com.eatup.commercial.service.sale.SaleService;
import com.eatup.commercial.util.json.MapperJsonObjeto;
import java.util.Optional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SalePatchConsumer {

    private final SaleService saleService;
    private final MapperJsonObjeto mapperJsonObjeto;

    public SalePatchConsumer(SaleService saleService, MapperJsonObjeto mapperJsonObjeto) {
        this.saleService = saleService;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

    @RabbitListener(queues = "${sales.patch.request.queue}")
    public void handleSalePatch(String message) {
        Optional<SalePatchRequestedMessage> dtoOpt = mapperJsonObjeto.ejecutar(message, SalePatchRequestedMessage.class);
        dtoOpt.ifPresent(saleService::applyPatch);
    }
}
