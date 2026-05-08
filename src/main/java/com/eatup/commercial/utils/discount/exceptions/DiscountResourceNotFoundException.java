package com.eatup.commercial.utils.discount.exceptions;

public class DiscountResourceNotFoundException extends DiscountApiException {

    public DiscountResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}