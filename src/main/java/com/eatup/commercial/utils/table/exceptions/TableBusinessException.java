package com.eatup.commercial.utils.table.exceptions;

public class TableBusinessException extends TableApiException {

    public TableBusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}