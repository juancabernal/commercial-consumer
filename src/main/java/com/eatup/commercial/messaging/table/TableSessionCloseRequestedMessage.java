package com.eatup.commercial.messaging.table;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableSessionCloseRequestedMessage {

    private String tableId;
    private UUID saleId;
    private String sellerId;
    private UUID locationId;
    private String message;
}
