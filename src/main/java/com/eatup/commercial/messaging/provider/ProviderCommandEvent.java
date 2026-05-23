package com.eatup.commercial.messaging.provider;

import java.util.Map;

public class ProviderCommandEvent {

    private String eventType;
    private String providerId;
    private Map<String, Object> payload;

    public ProviderCommandEvent() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
}