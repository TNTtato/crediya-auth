package com.crediya.security.adapters;

import com.crediya.model.user.gateways.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    @Override
    public String encode(String password) {
        return encoder.encode(password);
    }

    @Override
    public Boolean match(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
