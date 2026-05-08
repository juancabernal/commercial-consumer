package com.eatup.commercial.utils.discount.exceptions;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class DiscountApiException extends RuntimeException {

    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Bogota");

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected DiscountApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now(BUSINESS_ZONE);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}