package com.crediya.r2dbc;

import com.crediya.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Integer>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<UsuarioEntity> findByEmail(String email);
}
