package com.eatup.commercial.exception;

public class InvalidSaleStatusTransitionException extends RuntimeException {

    public InvalidSaleStatusTransitionException(String message) {
        super(message);
    }
}
