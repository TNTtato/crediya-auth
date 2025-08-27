package com.crediya.usecase.registeruser;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserUseCaseTest {

    UserRepository repository;

    RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        useCase = new RegisterUserUseCase(repository);
    }

    @ParameterizedTest
    @MethodSource("registerTestCases")
    void shouldRegisterUser(User input, User saved) {
        Mockito.when(repository.save(Mockito.any())).thenReturn(Mono.just(saved));
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Mono.empty());
        Mockito.when(repository.findByCardId(Mockito.anyString())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(input))
                .expectNextMatches(user -> user.getLastName().equals(input.getLastName()))
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("missingFieldsTestCases")
    @DisplayName("Should throw IllegalArgumentsException when validation fails")
    void shouldThrowIllegalArgumentsExceptionMissingFields(User in, String errMessage) {
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(t -> {
                    assertInstanceOf(UserValidationException.class, t);
                    assertEquals(((UserValidationException) t).getCauses().toString(), errMessage);
                })
                .verify();
    }

    @ParameterizedTest
    @MethodSource("businessValidationsTestCases")
    @DisplayName("If business validations fails, such as existing email or base salary out of range, throw")
    void shouldThrowBusinessException(User input, String errMessage, Class<? extends Throwable> expected) {
        Mockito.when(repository.findByEmail("doe@test.com")).thenReturn(Mono.just(new User()));
        Mockito.when(repository.findByEmail("doe@example.com")).thenReturn(Mono.empty());
        Mockito.when(repository.findByCardId(Mockito.anyString())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(input))
                .expectErrorSatisfies(t -> {
                    assertInstanceOf(expected, t);
                    assertTrue(t.getMessage().contains(errMessage));
                })
                .verify();
    }

    static Stream<Arguments> businessValidationsTestCases() {
        User existingMail = new User(1, "John", "doe", "doe@test.com", "CC1234", "+57 1234", 1, 3000000.0);
        User bellowSalaryRange = new User(1, "John", "doe", "doe@example.com", "CC1234", "+57 1234", 1, -1.0);
        User aboveSalaryRange = new User(1, "John", "doe", "doe@example.com", "CC1234", "+57 1234", 1, 15000001.0);
        return Stream.of(
                Arguments.of(existingMail, "Email already in use", EmailAlreadyInUseException.class),
                Arguments.of(bellowSalaryRange, "Base salary not in range", NotValidBaseSalaryException.class),
                Arguments.of(aboveSalaryRange, "Base salary not in range", NotValidBaseSalaryException.class)
        );
    }

    static Stream<Arguments> missingFieldsTestCases() {
        User emptyRequired = new User(1, "", "", "", "", "+57 1234", 1, 3000000.0);
        User allRequiredFieldsMissing = new User();
        User nullUser  = null;
        return Stream.of(
                Arguments.of(allRequiredFieldsMissing, "[Email is required, Full name is required, ID document is required, Base salary is required]"),
                Arguments.of(emptyRequired, "[Email is required, Full name is required, ID document is required]"),
                Arguments.of(nullUser, "[User is null]")
        );
    }

    static Stream<Arguments> registerTestCases() {
        return Stream.of(
                Arguments.of(new User(1, "John", "Doe", "jdoe@example.com", "CC11111", "+12345", 1, 3000000.0),
                            new User(1, "John", "Doe", "jdoe@example.com", "CC11111", "+12345", 1, 3000000.0)
                )
        );
    }
}