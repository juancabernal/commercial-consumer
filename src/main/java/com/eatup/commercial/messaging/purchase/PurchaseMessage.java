package com.eatup.commercial.messaging.purchase;

import com.eatup.commercial.domain.purchase.PurchaseAction;
import com.eatup.commercial.domain.purchase.PurchaseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseMessage {

    private UUID purchaseId;
    private String orderNumber;
    private UUID providerId;
    private UUID locationId;
    private List<PurchaseItemMessage> items;
    private BigDecimal total;
    private PurchaseStatus status;
    private PurchaseAction action;
    private LocalDateTime eventDate;

}
