package com.eatup.commercial.exception;

public class SaleUpdateProcessingException extends RuntimeException {

    public SaleUpdateProcessingException(String message) {
        super(message);
    }

    public SaleUpdateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
