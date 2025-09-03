package com.crediya.model.exception;

public class AuthException extends IllegalArgumentException{
    public AuthException() {
        super("Access denied: Authentication Failed");
    }
}
