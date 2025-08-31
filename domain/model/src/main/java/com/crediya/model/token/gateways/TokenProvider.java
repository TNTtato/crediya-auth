package com.crediya.model.token.gateways;

import com.crediya.model.token.Token;
import com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface TokenProvider {
    Mono<Token> generateToken(User user);
}
