package com.crediya.api.config;

import com.crediya.usecase.registeruser.CardIdAlreadyInUseException;
import com.crediya.usecase.registeruser.EmailAlreadyInUseException;
import com.crediya.usecase.registeruser.NotValidBaseSalaryException;
import com.crediya.usecase.registeruser.UserValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebTestClient webTestClient;
    private ErrorAttributes errorAttributes;
    private ServerRequest serverRequest;

    @BeforeEach
    void setUp() throws URISyntaxException {
        errorAttributes = mock(ErrorAttributes.class);
        WebProperties.Resources resources = mock(WebProperties.Resources.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);

        // Configure the ApplicationContext mock to return a valid ClassLoader
        when(applicationContext.getClassLoader()).thenReturn(this.getClass().getClassLoader());

        ServerCodecConfigurer configurer = ServerCodecConfigurer.create();

        exceptionHandler = new GlobalExceptionHandler(
                errorAttributes,
                resources,
                applicationContext,
                configurer
        );

        serverRequest = mock(ServerRequest.class);
        when(serverRequest.uri()).thenReturn(new URI("http://localhost/api/v1/usuarios"));

        // Configure WebTestClient
        RouterFunction<ServerResponse> routerFunction = exceptionHandler.getRoutingFunction(errorAttributes);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldHandleEmailAlreadyInUseException() {
        // Arrange
        EmailAlreadyInUseException exception = new EmailAlreadyInUseException("Email already in use");
        when(errorAttributes.getError(any(ServerRequest.class))).thenReturn(exception);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Email already in use")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.causes").doesNotExist();
    }

    @Test
    void shouldHandleCardIdAlreadyInUseException() {
        // Arrange
        CardIdAlreadyInUseException exception = new CardIdAlreadyInUseException("Card ID already in use");
        when(errorAttributes.getError(any(ServerRequest.class))).thenReturn(exception);

        // Act & Assert
        webTestClient.get()
                .uri("[root]")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Card ID already in use")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.causes").doesNotExist();
    }

    @Test
    void shouldHandleNotValidBaseSalaryException() {
        // Arrange
        NotValidBaseSalaryException exception = new NotValidBaseSalaryException("Invalid base salary");
        when(errorAttributes.getError(any(ServerRequest.class))).thenReturn(exception);

        // Act & Assert
        webTestClient.get()
                .uri("[root]")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid base salary")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.causes").doesNotExist();
    }

    @Test
    void shouldHandleUserValidationException() {
        // Arrange
        List<String> causes = List.of("Invalid name", "Invalid age");
        UserValidationException exception = new UserValidationException("User validation failed", causes);
        when(errorAttributes.getError(any(ServerRequest.class))).thenReturn(exception);

        // Act & Assert
        webTestClient.get()
                .uri("[root]")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("User validation failed")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.causes[0]").isEqualTo("Invalid name");
    }

    @Test
    void shouldHandleGenericException() {
        // Arrange
        RuntimeException exception = new RuntimeException("Unexpected error");
        when(errorAttributes.getError(any(ServerRequest.class))).thenReturn(exception);

        // Act & Assert
        webTestClient.get()
                .uri("[root]")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Unexpected error")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.causes").doesNotExist();
    }
}