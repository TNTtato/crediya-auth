package com.crediya.api;

import com.crediya.api.model.RegisterUserRequest;
import com.crediya.model.user.User;
import com.crediya.usecase.registeruser.RegisterUserUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {RouterRest.class, HandlerV1.class, HandlerV2.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean private RegisterUserUseCase registerUserUseCase;
    @MockitoBean private ObjectMapper objectMapper;

    @Test
    void testListenPOSTUseCaseV1() {

        User user = new User(1,"John", "Doe", "jdoe@test.com", "CC1234", "+1234", 2, 3000000.0);
        RegisterUserRequest rqBody = new RegisterUserRequest("John", "Doe", "jdoe@test.com", "CC1234", "+1234", 2, 3000000.0);
        Mockito.when(registerUserUseCase.execute(Mockito.any())).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(rqBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .value(Assertions::assertNotNull
                );
    }

}
