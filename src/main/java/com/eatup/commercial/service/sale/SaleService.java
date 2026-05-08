package com.eatup.commercial.service.sale;

import com.eatup.commercial.messaging.sales.SaleCreateResponseMessage;
import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import com.eatup.commercial.messaging.sales.SaleUpdateResponseMessage;

public interface SaleService {
    void applyPatch(SalePatchRequestedMessage message);
    void applyCreateResponse(SaleCreateResponseMessage message);
    void applyUpdateResponse(SaleUpdateResponseMessage message);
}
