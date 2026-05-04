package com.eatup.commercial.messaging.sales;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalePatchRequestedMessage {
    private UUID saleId;
    private SalePatchRequest request;
}
