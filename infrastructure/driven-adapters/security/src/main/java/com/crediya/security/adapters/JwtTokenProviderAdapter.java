package com.crediya.security.adapters;

import com.crediya.model.token.Token;
import com.crediya.model.token.gateways.TokenProvider;
import com.crediya.model.user.User;
import com.crediya.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderAdapter implements TokenProvider {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Token> generateToken(User user) {
        return jwtProvider.generateToken(user);
    }
}
