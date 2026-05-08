package com.eatup.commercial.messaging.sales;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleRecipeResponseMessage {
    private UUID recipeId;
    private String lineDisplayName;
    private String recipeLineComment;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Boolean approved;
    private String message;
}
