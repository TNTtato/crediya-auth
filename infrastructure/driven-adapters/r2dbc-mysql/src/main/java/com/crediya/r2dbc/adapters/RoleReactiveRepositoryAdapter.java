package com.crediya.r2dbc.adapters;

import com.crediya.model.role.Role;
import com.crediya.model.role.gateways.RoleRepository;
import com.crediya.r2dbc.RoleReactiveRepository;
import com.crediya.r2dbc.entity.RoleEntity;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Integer,
        RoleReactiveRepository
        > implements RoleRepository {
    protected RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, r -> mapper.map(r, Role.class));
    }
}
