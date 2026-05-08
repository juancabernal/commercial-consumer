package com.eatup.commercial.utils.customerDiscount.exceptions;

public class CustomerDiscountBusinessException extends CustomerDiscountApiException {

    public CustomerDiscountBusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}