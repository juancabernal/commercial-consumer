package com.eatup.commercial.messaging.table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TableCommandEvent {

    private String eventType;
    private String tableId;
    private String sessionId;
    private String reservationId;
    private Map<String, Object> payload;
    private LocalDateTime occurredAt;
}
