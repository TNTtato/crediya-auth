package com.crediya.usecase.exception;

public class EmailAlreadyInUseException extends IllegalArgumentException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
