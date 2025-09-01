package com.crediya.usecase.exception;

import java.util.List;

public class UserValidationException extends IllegalArgumentException {
    private List<String> causes;
    public UserValidationException(String message, List<String> causes) {

        super(message);
        this.causes = causes;
    }

    public List<String> getCauses() {
        return causes;
    }
}
