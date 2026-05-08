package com.eatup.commercial.exception;

public class SaleDeleteProcessingException extends RuntimeException {

    public SaleDeleteProcessingException(String message) {
        super(message);
    }

    public SaleDeleteProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
