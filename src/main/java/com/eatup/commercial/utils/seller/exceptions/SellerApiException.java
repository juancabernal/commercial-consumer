package com.eatup.commercial.utils.seller.exceptions;

import java.time.LocalDateTime;

public abstract class SellerApiException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected SellerApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() { return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}