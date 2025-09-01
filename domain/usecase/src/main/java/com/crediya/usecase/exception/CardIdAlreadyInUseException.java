package com.crediya.usecase.exception;

public class CardIdAlreadyInUseException extends IllegalArgumentException{
    public CardIdAlreadyInUseException(String s) {
        super(s);
    }
}
