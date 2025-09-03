package com.crediya.security.provider;

import com.crediya.model.role.gateways.RoleRepository;
import com.crediya.model.token.Token;
import com.crediya.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private final RoleRepository roleRepository;

    public Mono<Token> generateToken(User user) {
        return roleRepository.findById(user.getRoleId()).flatMap(r -> Mono.defer(() -> {
            JwtBuilder builder = Jwts.builder();
            builder.subject(user.getEmail());
            builder.claims(Map.of(
                    "role", r.getName(),
                    "cardId", user.getCardId(),
                    "userId", user.getUserId()
            ));
            builder.issuedAt(new Date());
            builder.expiration(new Date(System.currentTimeMillis() + expiration));
            builder.signWith(getKey(secret));
            return Mono.just(new Token(builder.compact()));
        }))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("No role found"))));
    }

    public String getSubject(String token) {
        return this.getClaims(token).getSubject();
    }

    public Boolean isValid(String token) {
        Date expiration = this.getClaims(token).getExpiration();
        return expiration != null && expiration.after(new Date());
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(getKey(secret)).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
