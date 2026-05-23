package com.eatup.commercial.utils.provider.exceptions;

public class ProviderNotFoundException extends ProviderApiException {
    public ProviderNotFoundException(String message) {
        super(message, "PROVIDER_NOT_FOUND");
    }
}