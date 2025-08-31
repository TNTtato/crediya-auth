package com.crediya.config;

import com.crediya.model.role.gateways.RoleRepository;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.usecase.registeruser.RegisterUserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class UseCasesConfigTest {

    @Test
    @DisplayName("Should register SignUpUseCase bean in application context")
    void testSignUpUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {

            RegisterUserUseCase signUpUseCase = context.getBean(RegisterUserUseCase.class);
            assertNotNull(signUpUseCase, "SignUpUseCase bean should be registered");
        }
    }


    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        @Bean UserRepository userRepository() { return mock(UserRepository.class); }
        @Bean RoleRepository roleRepository() { return mock(RoleRepository.class); }
    }
}