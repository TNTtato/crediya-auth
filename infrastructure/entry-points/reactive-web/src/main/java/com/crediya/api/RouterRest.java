package com.crediya.api;

import com.crediya.api.model.ApiError;
import com.crediya.api.model.RegisterUserRequest;
import com.crediya.api.opendocs.LoginDoc;
import com.crediya.api.opendocs.RegisterUserDoc;
import com.crediya.model.user.User;
import com.crediya.model.utils.AppRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = RegisterUserDoc.class,
                    beanMethod = "registerUserDoc"
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = LoginDoc.class,
                    beanMethod = "loginDoc"
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerV1 handlerV1, HandlerV2 handlerV2, AppRoutes routes) {
        return RouterFunctions
            .route()
                .path(routes.baseV1(), builder -> builder.POST(routes.user(), handlerV1::listenRegisterUserUseCase))
                .path(routes.baseV1(), builder -> builder.POST(routes.login(), handlerV1::listenLoginUseCase))
                .build();
        }
}
