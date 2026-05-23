package com.eatup.commercial.utils.provider.exceptions;

public class ProviderBusinessException extends ProviderApiException {
    public ProviderBusinessException(String message) {
        super(message, "PROVIDER_BUSINESS_ERROR");
    }
}