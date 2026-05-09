package com.eatup.commercial.utils.discount.exceptions;

public class DiscountValidationException extends DiscountApiException {

    public DiscountValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}