package com.eatup.commercial.messaging.provider;

import com.eatup.commercial.dto.provider.ProviderDTO;
import java.util.Map;

public class ProviderCommandEvent {

    private String eventType;
    private String providerId;
    private ProviderDTO payload;
    private Map<String, Object> statusPayload;

    public ProviderCommandEvent() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public ProviderDTO getPayload() { return payload; }
    public void setPayload(ProviderDTO payload) { this.payload = payload; }

    public Map<String, Object> getStatusPayload() { return statusPayload; }
    public void setStatusPayload(Map<String, Object> statusPayload) { this.statusPayload = statusPayload; }
}