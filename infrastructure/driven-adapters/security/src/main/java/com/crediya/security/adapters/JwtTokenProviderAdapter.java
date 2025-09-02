package com.crediya.security.adapters;

import com.crediya.model.token.Token;
import com.crediya.model.token.gateways.TokenProvider;
import com.crediya.model.user.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenProviderAdapter implements TokenProvider {

    @Override
    public Mono<Token> generateToken(User user) {
        return Mono.just(new Token("1234"));
    }
}
