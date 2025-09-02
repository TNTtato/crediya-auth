package com.crediya.usecase.login;

import com.crediya.model.token.Token;
import com.crediya.model.token.gateways.TokenProvider;
import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoder;
import com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LoginUseCaseTest {

    final static String FIXED_TOKEN = "1234567890";
    final static String FIXED_PASSWORD = "1234567890";
    final static String FIXED_EMAIL = "jdoe@example.com";

    final static User FIXED_USER = new User(1, "John", "Doe", "jdoe@exmaple.com", "CC1234", "+12345", 2, 3000000.0, "12345");

    TokenProvider tokenProvider;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    LoginUseCase useCase;

    @BeforeEach
    void setUp() {
        tokenProvider = Mockito.mock(TokenProvider.class);
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        useCase = new LoginUseCase(tokenProvider, userRepository, passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("successLoginTestCases")
    void shouldReturnToken(User user, Token token, String email, String password) {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Mono.just(user));
        Mockito.when(passwordEncoder.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(tokenProvider.generateToken(Mockito.any(User.class))).thenReturn(Mono.just(token));


        StepVerifier.create(useCase.execute(email, password))
                .expectNextMatches(t -> t.getToken().equals(FIXED_TOKEN))
                .verifyComplete();

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.times(1)).match(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(tokenProvider, Mockito.times(1)).generateToken(Mockito.any(User.class));
    }

    @Test
    void shouldReturnUserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(FIXED_EMAIL, FIXED_PASSWORD))
                .expectErrorSatisfies(e -> assertTrue(e.getMessage().contains("User not found")))
                .verify();

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
    }

    @Test
    void shouldReturnInvalidPassword() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Mono.just(FIXED_USER));
        Mockito.when(passwordEncoder.match(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        StepVerifier.create(useCase.execute(FIXED_EMAIL, FIXED_PASSWORD))
                .expectErrorSatisfies(e -> assertTrue(e.getMessage().contains("Invalid password")))
                .verify();

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
    }

    static Stream<Arguments> successLoginTestCases() {
        return Stream.of(
                Arguments.of(
                        FIXED_USER,
                        new Token(FIXED_TOKEN),
                        FIXED_USER.getEmail(),
                        FIXED_USER.getPassword()
                )
        );
    }
}

