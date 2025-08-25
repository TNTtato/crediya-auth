package com.crediya.model.usuario.gateways;

import com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Usuario> save(Usuario usuario);
    Flux<Usuario> findAll();
    Mono<Usuario> findById(Integer id);
}
