package com.eatup.commercial.messaging.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateMessage {

    private UUID locationId;
    private String productId;
    private BigDecimal quantity;
}