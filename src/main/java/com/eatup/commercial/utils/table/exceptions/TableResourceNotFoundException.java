package com.eatup.commercial.utils.table.exceptions;

public class TableResourceNotFoundException extends TableApiException {

    public TableResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}