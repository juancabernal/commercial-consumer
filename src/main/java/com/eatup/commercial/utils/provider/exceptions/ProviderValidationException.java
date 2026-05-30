package com.eatup.commercial.utils.provider.exceptions;

public class ProviderValidationException extends ProviderApiException {
    public ProviderValidationException(String message) {
        super(message, "PROVIDER_VALIDATION_ERROR");
    }
}