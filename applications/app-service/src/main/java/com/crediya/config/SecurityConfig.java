package com.crediya.config;

import com.crediya.security.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;

    @Value("${springdoc.swagger-ui.path}")
    private String apiSwaggerPath;

    @Value("${springdoc.webjars.prefix}")
    private String apiWebjarsPath;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtFilter jwtFilter,
            ServerAuthenticationEntryPoint authenticationEntryPoint,
            ServerAccessDeniedHandler accessDeniedHandler,
            RoutesConfig routesConfig
    ) {
        String loginRoute = routesConfig.baseV1() + routesConfig.login();
        String usersRoute = routesConfig.baseV1() + routesConfig.user();
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(
                                loginRoute,
                                apiDocsPath + "/**",
                                apiSwaggerPath + "/**",
                                apiWebjarsPath + "/**",
                                "/swagger-ui/**"
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, usersRoute).hasAnyRole("ADMIN", "ASESOR")
                        .anyExchange()
                        .authenticated())
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }
}