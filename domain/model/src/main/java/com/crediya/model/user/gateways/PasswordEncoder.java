package com.crediya.model.user.gateways;

public interface PasswordEncoder {

    String encode(String password);
    Boolean match(String raw, String encoded);
}
