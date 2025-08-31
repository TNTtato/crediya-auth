package com.crediya.model.role.gateways;

import com.crediya.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> save(Role role);
    Flux<Role> findAll();
    Mono<Role> findById(Integer id);
}
