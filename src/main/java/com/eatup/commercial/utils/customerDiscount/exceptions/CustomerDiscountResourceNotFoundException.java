package com.eatup.commercial.utils.customerDiscount.exceptions;

public class CustomerDiscountResourceNotFoundException extends CustomerDiscountApiException {

    public CustomerDiscountResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}