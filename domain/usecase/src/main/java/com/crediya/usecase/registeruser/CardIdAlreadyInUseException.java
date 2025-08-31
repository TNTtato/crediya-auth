package com.crediya.usecase.registeruser;

import java.util.function.Supplier;

public class CardIdAlreadyInUseException extends IllegalArgumentException{
    public CardIdAlreadyInUseException(String s) {
        super(s);
    }
}
