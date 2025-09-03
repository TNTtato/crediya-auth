package com.crediya.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "routes")
public record RoutesConfig(
        String baseV1,
        String login,
        String user
) {
}
