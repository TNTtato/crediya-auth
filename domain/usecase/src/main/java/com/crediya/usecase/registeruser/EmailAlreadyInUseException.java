package com.crediya.usecase.registeruser;

public class EmailAlreadyInUseException extends IllegalArgumentException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
