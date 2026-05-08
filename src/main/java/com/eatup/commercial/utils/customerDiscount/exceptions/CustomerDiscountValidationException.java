package com.eatup.commercial.utils.customerDiscount.exceptions;

public class CustomerDiscountValidationException extends CustomerDiscountApiException {

    public CustomerDiscountValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}