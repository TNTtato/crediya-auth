package com.crediya.usecase.registeruser;

public class NotValidBaseSalaryException extends RuntimeException {
    public NotValidBaseSalaryException(String message) {
        super(message);
    }
}
