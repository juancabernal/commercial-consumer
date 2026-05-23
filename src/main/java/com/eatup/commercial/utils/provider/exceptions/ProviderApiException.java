package com.eatup.commercial.utils.provider.exceptions;

import java.time.LocalDateTime;

public abstract class ProviderApiException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected ProviderApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() { return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}