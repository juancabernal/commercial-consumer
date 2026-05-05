package com.eatup.commercial.messaging.purchase;

import com.eatup.commercial.service.purchase.PurchaseProcessorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMessageConsumer {

    private final PurchaseProcessorService purchaseProcessorService;

    public PurchaseMessageConsumer(PurchaseProcessorService purchaseProcessorService) {
        this.purchaseProcessorService = purchaseProcessorService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.purchase}")
    public void consume(PurchaseMessage message) {
        switch (message.getAction()) {
            case CREATED       -> purchaseProcessorService.processPurchaseCreated(message);
            case UPDATED       -> purchaseProcessorService.processPurchaseUpdated(message);
            case STATUS_UPDATED -> purchaseProcessorService.processPurchaseStatusUpdated(message);
            case DELETED       -> purchaseProcessorService.processPurchaseDeleted(message);
            default -> throw new IllegalArgumentException(
                    "Unknown action: " + message.getAction());
        }
    }

}
