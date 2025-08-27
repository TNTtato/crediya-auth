package com.crediya.r2dbc;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.r2dbc.entity.UserEntity;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
    Integer,
        UserReactiveRepository
> implements UserRepository {

    private final TransactionalOperator txOp;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, TransactionalOperator txOp) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.txOp = txOp;
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user).as(txOp::transactional);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(o -> mapper.map(o, User.class));
    }

    @Override
    public Mono<User> findByCardId(String cardId) {
        return repository.findByCardId(cardId).map(o -> mapper.map(o, User.class));
    }
}
