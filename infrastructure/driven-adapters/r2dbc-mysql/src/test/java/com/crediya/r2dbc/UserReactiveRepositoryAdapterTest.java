package com.crediya.r2dbc;

import com.crediya.model.user.User;
import com.crediya.r2dbc.adapters.UserReactiveRepositoryAdapter;
import com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    TransactionalOperator txOp;

    @Test
    void mustFindValueById() {

        UserEntity entity = new UserEntity(1, "John", "Doe", "jdoe@example.com", "+1234", "DNI123", 3000000.0, 2);
        User user = new User(1, "John", "Doe", "jdoe@example.com", "+1234", "DNI123", 2, 3000000.0);
        when(repository.findById(1)).thenReturn(Mono.just(entity));
        when(mapper.map(any(), eq(User.class))).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(1);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity entity = new UserEntity(1, "John", "Doe", "jdoe@example.com", "+1234", "DNI123", 3000000.0, 2);
        User user = new User(1, "John", "Doe", "jdoe@example.com", "+1234", "DNI123", 2, 3000000.0);

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(any(), eq(User.class))).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        UserEntity entity = new UserEntity(1, "John", "Doe", "jdoe@example.com", "+1234", "DNI123", 3000000.0, 2);
        User user = new User(1, "John", "Doe", "jdoe@example.com", "+1234", "DNI123", 2, 3000000.0);

        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(any(), eq(UserEntity.class))).thenReturn(entity);
        when(mapper.map(any(), eq(User.class))).thenReturn(user);
        when(txOp.transactional((Mono<Object>) any())).thenReturn(Mono.empty());

        when(txOp.transactional((Mono<User>) any())).thenAnswer(inv -> inv.getArgument(0));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals(user.getEmail()))
                .verifyComplete();

        // Verificar que se aplicó transactional
        verify(txOp).transactional((Mono<User>) any());
    }

}
