package com.eatup.commercial.utils.seller.exceptions;

public class SellerBusinessException extends SellerApiException {
    public SellerBusinessException(String message) {
        super(message, "SELLER_BUSINESS_ERROR");
    }
}