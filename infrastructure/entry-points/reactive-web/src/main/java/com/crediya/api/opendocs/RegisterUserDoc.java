package com.crediya.api.opendocs;

import com.crediya.api.model.ApiError;
import com.crediya.api.model.RegisterUserRequest;
import com.crediya.api.model.RegisterUserResponse;
import com.crediya.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Schema(description = "Documentation for user registration endpoint")
public class RegisterUserDoc {

    @Operation(
            operationId = "registerUser",
            summary = "Register a new user",
            tags = {"User"},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterUserResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation Errors", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "409", description = "User Already Exists", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public Mono<RegisterUserResponse> registerUserDoc() {
        return Mono.empty();
    }
}
