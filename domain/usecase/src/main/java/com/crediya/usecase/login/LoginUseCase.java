package com.crediya.usecase.login;

import com.crediya.model.token.Token;
import com.crediya.model.token.gateways.TokenProvider;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoder;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.usecase.exception.InvalidPasswordException;
import com.crediya.usecase.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor

public class LoginUseCase {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<Token> execute(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException("User not found!"))))
                .flatMap(u -> validatePassword(u, password))
                .flatMap(tokenProvider::generateToken);
    }

    private Mono<User> validatePassword(User user, String raw) {
        return Mono.just(user)
                .filter(u -> passwordEncoder.match(raw, u.getPassword()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new InvalidPasswordException("Invalid password!"))));
    }
}
