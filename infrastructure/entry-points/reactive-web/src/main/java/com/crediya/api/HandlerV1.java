package com.crediya.api;

import com.crediya.api.model.LoginRequest;
import com.crediya.api.model.RegisterUserRequest;
import com.crediya.api.util.MapperUtil;
import com.crediya.usecase.login.LoginUseCase;
import com.crediya.usecase.registeruser.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
//import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerV1 {
    private final RegisterUserUseCase useCase;
    private final LoginUseCase loginUseCase;
    private final ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(HandlerV1.class);

    //@PreAuthorize("hasRole('permissionPOST')")
    public Mono<ServerResponse> listenRegisterUserUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterUserRequest.class)
                .flatMap(r -> {
                    log.info("Received RegisterUserRequest");
                    return useCase.execute(MapperUtil.fromRequestToUserDomain(r));
                })
                .flatMap(
                saved -> ServerResponse
                        .created(serverRequest.uri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(MapperUtil.fromUserDomainToResponse(saved)));
    }

    public Mono<ServerResponse> listenLoginUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(r -> {
                    log.info("Received LoginRequest");
                    return loginUseCase.execute(r.email(), r.password());
                })
                .flatMap(t -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(t.getToken()));
    }
}
