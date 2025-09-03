package com.crediya.config;

import com.crediya.model.utils.AppRoutes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RoutesConfig.class})
public class TransversalBeans {

    @Bean
    AppRoutes routes(RoutesConfig routesConfig) {
        return new AppRoutes(
                routesConfig.baseV1(),
                routesConfig.login(),
                routesConfig.user()
        );
    }
}
