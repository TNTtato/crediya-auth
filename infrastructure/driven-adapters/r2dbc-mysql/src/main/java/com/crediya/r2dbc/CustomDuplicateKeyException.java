package com.crediya.r2dbc;

import org.springframework.dao.DuplicateKeyException;

public class CustomDuplicateKeyException extends DuplicateKeyException {
    public CustomDuplicateKeyException(String message) {
        super(message);
    }
}
