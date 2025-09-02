package com.crediya.api;

import com.crediya.api.model.ApiError;
import com.crediya.api.model.RegisterUserRequest;
import com.crediya.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
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
                    beanClass = HandlerV1.class,
                    beanMethod = "listenRegisterUserUseCase",
                    operation = @Operation(
                            operationId = "registerUser",
                            summary = "Register a new user",
                            tags = {"User"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = RegisterUserRequest.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Nuevo usuario",
                                                            summary = "Ejemplo de registro de usuario",
                                                            value = """
                                {
                                  "nombre": "Juan",
                                  "apellido": "Pérez",
                                  "email": "juan.perez@test.com",
                                  "documento_identidad": "12345678",
                                  "telefono": "3001234567",
                                  "id_rol": 2,
                                  "salario_base": 3000000.0
                                }
                                """
                                                    )
                                            }
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User created successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = User.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Usuario creado",
                                                                    summary = "Ejemplo de respuesta de creación",
                                                                    value = """
                                    {
                                      "userId": 1,
                                      "name": "Juan",
                                      "lastName": "Pérez",
                                      "email": "juan.perez@test.com",
                                      "idCard": "12345678",
                                      "phone": "3001234567",
                                      "roleId": 2,
                                      "baseSalary": 5000.0
                                    }
                                    """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Validation Errors", content = @Content(schema = @Schema(implementation = ApiError.class))),
                                    @ApiResponse(responseCode = "409", description = "User Already Exists", content = @Content(schema = @Schema(implementation = ApiError.class))),
                                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class)))
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerV1 handlerV1, HandlerV2 handlerV2) {
        return RouterFunctions
            .route()
                .path("/api/v1", builder -> builder.POST("/usuarios", handlerV1::listenRegisterUserUseCase))
                .path("/api/v1", builder -> builder.POST("/login", handlerV1::listenLoginUseCase))
                .build();
        }
}
