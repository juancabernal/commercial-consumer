package com.eatup.commercial.utils.seller.exceptions;

public class SellerValidationException extends SellerApiException {
    public SellerValidationException(String message) {
        super(message, "SELLER_VALIDATION_ERROR");
    }
}