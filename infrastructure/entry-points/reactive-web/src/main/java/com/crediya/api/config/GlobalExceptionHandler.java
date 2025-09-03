package com.crediya.api.config;

import com.crediya.api.model.ApiError;
import com.crediya.model.exception.AccessDeniedException;
import com.crediya.model.exception.AuthException;
import com.crediya.usecase.exception.*;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties.Resources resources,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        setMessageReaders(configurer.getReaders());
        setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderException);
    }

    private Mono<ServerResponse> renderException(ServerRequest request) {

        Throwable error = getError(request);

        if (error instanceof EmailAlreadyInUseException e) {
            return buildErrorResponse(HttpStatus.CONFLICT, request, e.getMessage());
        }
        if (error instanceof CardIdAlreadyInUseException e) {
            return buildErrorResponse(HttpStatus.CONFLICT, request, e.getMessage());
        }
        if (error instanceof NotValidBaseSalaryException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, request, e.getMessage());
        }
        if (error instanceof UserValidationException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, request, e.getMessage(), e.getCauses());
        }

        if (error instanceof InvalidPasswordException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, request, e.getMessage());
        }

        if (error instanceof UserNotFoundException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, request, e.getMessage());
        }

        if (error instanceof AuthException  e) {
            return buildErrorResponse(HttpStatus.FORBIDDEN, request, e.getMessage());
        }

        if (error instanceof AccessDeniedException e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, request, e.getMessage());
        }

        // fallback
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, error.getLocalizedMessage());
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus status, ServerRequest request, String message) {
        return buildErrorResponse(status, request, message, null);
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus status, ServerRequest request, String message, List<String> causes) {
        ApiError apiError = new ApiError(
                message,
                request.uri().toString(),
                new Date(),
                causes
        );
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiError);
    }
}
