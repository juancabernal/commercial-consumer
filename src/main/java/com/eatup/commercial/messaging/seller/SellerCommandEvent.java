package com.eatup.commercial.messaging.seller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SellerCommandEvent {

    private String eventType;
    private String sellerId;
    private LocalDateTime occurredAt;
    private Map<String, Object> payload;
}