package com.eatup.commercial.messaging.sales;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleUpdateResponseMessage {
    private UUID saleId;
    private SaleUpdatePayloadMessage sale;
}
