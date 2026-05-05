package com.eatup.commercial.service.purchase;

import com.eatup.commercial.messaging.purchase.PurchaseMessage;

public interface PurchaseProcessorService {

    void processPurchaseCreated(PurchaseMessage message);
    void processPurchaseUpdated(PurchaseMessage message);
    void processPurchaseStatusUpdated(PurchaseMessage message);
    void processPurchaseDeleted(PurchaseMessage message);

}
