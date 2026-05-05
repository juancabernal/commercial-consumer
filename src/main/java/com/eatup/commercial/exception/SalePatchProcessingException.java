package com.eatup.commercial.exception;

public class SalePatchProcessingException extends RuntimeException {

    public SalePatchProcessingException(String message) {
        super(message);
    }

    public SalePatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
