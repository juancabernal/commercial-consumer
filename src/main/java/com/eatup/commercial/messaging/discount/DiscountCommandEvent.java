package com.eatup.commercial.messaging.discount;

import java.time.LocalDateTime;
import java.util.Map;

public class DiscountCommandEvent {
    private String eventType;
    private String discountId;
    private LocalDateTime occurredAt;
    private Map<String, Object> payload;

    public DiscountCommandEvent() {}
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDiscountId() { return discountId; }
    public void setDiscountId(String discountId) { this.discountId = discountId; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
}