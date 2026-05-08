package com.eatup.commercial.messaging.sales;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleUpdatePayloadMessage {
    private UUID idMessage;
    private UUID locationId;
    private String sellerId;
    private String tableId;
    private List<SaleRecipeResponseMessage> recipes;
}
