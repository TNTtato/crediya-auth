package com.crediya.usecase.registeruser;

import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserUseCaseTest {

    UsuarioRepository repository;

    RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UsuarioRepository.class);
        useCase = new RegisterUserUseCase(repository);
    }

    @ParameterizedTest
    @MethodSource("registerTestCases")
    void shouldRegisterUser(Usuario input, Usuario saved) {
        Mockito.when(repository.save(Mockito.any())).thenReturn(Mono.just(saved));
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(input))
                .expectNextMatches(user -> user.getApellido().equals(input.getApellido()))
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("missingFieldsTestCases")
    @DisplayName("Should throw IllegalArgumentsException when validation fails")
    void shouldThrowIllegalArgumentsExceptionMissingFields(Usuario in, String errMessage) {
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(t -> {
                    assertInstanceOf(UserValidationException.class, t);
                    assertTrue(t.getMessage().contains(errMessage));
                })
                .verify();
    }

    @ParameterizedTest
    @MethodSource("businessValidationsTestCases")
    @DisplayName("If business validations fails, such as existing email or base salary out of range, throw")
    void shouldThrowBusinessException(Usuario input, String errMessage, Class<? extends Throwable> expected) {
        Mockito.when(repository.findByEmail("doe@test.com")).thenReturn(Mono.just(new Usuario()));
        Mockito.when(repository.findByEmail("doe@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(input))
                .expectErrorSatisfies(t -> {
                    assertInstanceOf(expected, t);
                    assertTrue(t.getMessage().contains(errMessage));
                })
                .verify();
    }

    static Stream<Arguments> businessValidationsTestCases() {
        Usuario existingMail = new Usuario(1, "John", "doe", "doe@test.com", "CC1234", "+57 1234", 1, 3000000.0);
        Usuario bellowSalaryRange = new Usuario(1, "John", "doe", "doe@example.com", "CC1234", "+57 1234", 1, -1.0);
        Usuario aboveSalaryRange = new Usuario(1, "John", "doe", "doe@example.com", "CC1234", "+57 1234", 1, 15000001.0);
        return Stream.of(
                Arguments.of(existingMail, "Email already in use", EmailAlreadyInUseException.class),
                Arguments.of(bellowSalaryRange, "Base salary not in range", NotValidBaseSalaryException.class),
                Arguments.of(aboveSalaryRange, "Base salary not in range", NotValidBaseSalaryException.class)
        );
    }

    static Stream<Arguments> missingFieldsTestCases() {
        Usuario emptyRequired = new Usuario(1, "", "", "", "", "+57 1234", 1, 3000000.0);
        Usuario allRequiredFieldsMissing = new Usuario();
        Usuario nullUser  = null;
        return Stream.of(
                Arguments.of(allRequiredFieldsMissing, "[Email is required, Full name is required, ID document is required, Base salary is required]"),
                Arguments.of(emptyRequired, "[Email is required, Full name is required, ID document is required]"),
                Arguments.of(nullUser, "[User is null]")
        );
    }

    static Stream<Arguments> registerTestCases() {
        return Stream.of(
                Arguments.of(new Usuario(1, "John", "Doe", "jdoe@example.com", "CC11111", "+12345", 1, 3000000.0),
                            new Usuario(1, "John", "Doe", "jdoe@example.com", "CC11111", "+12345", 1, 3000000.0)
                )
        );
    }
}