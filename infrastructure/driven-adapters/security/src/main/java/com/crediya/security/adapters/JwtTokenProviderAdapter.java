package com.crediya.security.adapters;

import com.crediya.model.token.Token;
import com.crediya.model.token.gateways.TokenProvider;
import com.crediya.model.user.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderAdapter implements TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    @Override
    public Mono<Token> generateToken(User user) {
        return Mono.defer(() -> {
                   JwtBuilder builder = Jwts.builder();
                   builder.subject(user.getEmail());
                   builder.claims(Map.of(
                           "role", user.getRoleId(),
                           "cardId", user.getCardId(),
                           "userId", user.getUserId()
                   ));
                   builder.issuedAt(new Date());
                   builder.expiration(new Date(System.currentTimeMillis() + expiration));
                   builder.signWith(Keys.hmacShaKeyFor(secret.getBytes()));
                   return Mono.just(new Token(builder.compact()));
                });
    }
}
