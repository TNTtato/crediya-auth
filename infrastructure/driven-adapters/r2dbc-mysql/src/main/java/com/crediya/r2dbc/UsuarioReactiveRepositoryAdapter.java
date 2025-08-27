package com.crediya.r2dbc;

import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.r2dbc.entity.UsuarioEntity;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import com.sun.jdi.request.DuplicateRequestException;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.sql.SQLIntegrityConstraintViolationException;

@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario/* change for domain model */,
        UsuarioEntity/* change for adapter model */,
    Integer,
        UsuarioReactiveRepository
> implements UsuarioRepository {
    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    public Mono<Usuario> findByEmail(String email) {
        return repository.findByEmail(email).map(o -> mapper.map(o, Usuario.class));
    }
}
