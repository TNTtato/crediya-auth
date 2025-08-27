package com.crediya.api.config;

import com.crediya.usecase.registeruser.EmailAlreadyInUseException;
import com.crediya.usecase.registeruser.NotValidBaseSalaryException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@Configuration
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

    private Mono<ServerResponse> renderException(ServerRequest serverRequest) {
        Throwable error = getError(serverRequest);
        if (error instanceof EmailAlreadyInUseException eaiu)
            return ServerResponse.badRequest().bodyValue(Map.of(
                    "message", eaiu.getMessage(),
                    "uri", serverRequest.uri(),
                    "timestamp", new Date()
            ));

        if (error instanceof NotValidBaseSalaryException nvbs)
            return ServerResponse.badRequest().bodyValue(Map.of(
                    "message", nvbs.getMessage(),
                    "uri", serverRequest.uri(),
                    "timestamp", new Date()
            ));
        //DuplicateKeyException
        return ServerResponse.badRequest().bodyValue(Map.of("message", error.getLocalizedMessage()));
    }
}
