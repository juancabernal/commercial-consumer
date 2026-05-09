package com.eatup.commercial.utils.seller.exceptions;

public class SellerNotFoundException extends SellerApiException {
    public SellerNotFoundException(String message) {
        super(message, "SELLER_NOT_FOUND");
    }
}