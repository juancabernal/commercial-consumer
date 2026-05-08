package com.eatup.commercial.exception;

public class SaleCreateProcessingException extends RuntimeException {

    public SaleCreateProcessingException(String message) {
        super(message);
    }

    public SaleCreateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
