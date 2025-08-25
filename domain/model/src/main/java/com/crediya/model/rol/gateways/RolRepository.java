package com.crediya.model.rol.gateways;

import com.crediya.model.rol.Rol;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolRepository {
    Mono<Rol> save(Rol rol);
    Flux<Rol> findAll();
    Mono<Rol> findById(Integer id);
}
