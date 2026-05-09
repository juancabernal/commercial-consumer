package com.eatup.commercial.utils.discount.exceptions;

public class DiscountBusinessException extends DiscountApiException {

    public DiscountBusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}