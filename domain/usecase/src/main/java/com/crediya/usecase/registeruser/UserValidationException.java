package com.crediya.usecase.registeruser;

public class UserValidationException extends IllegalArgumentException {
    public UserValidationException(String message) {
        super(message);
    }
}
