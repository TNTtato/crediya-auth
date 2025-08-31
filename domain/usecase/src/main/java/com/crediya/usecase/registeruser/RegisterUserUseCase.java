package com.crediya.usecase.registeruser;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoder;
import com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public Mono<User> execute(User user) {

        List<String> validationErrors = validateEntry(user);
        if (!validationErrors.isEmpty()) {
            return Mono.error(new UserValidationException("User validation failed", validationErrors));
        }

        return userRepository.findByEmail(user.getEmail())
                .flatMap(exists -> Mono.<User>error(new EmailAlreadyInUseException("Email already in use")))
                .switchIfEmpty(Mono.defer(() -> {
                    if (notValidBaseSalary(user.getBaseSalary()))
                        throw new NotValidBaseSalaryException("Base salary not in range");
                    return userRepository.findByCardId(user.getCardId())
                            .flatMap(exists -> Mono.<User>error(new CardIdAlreadyInUseException("The user's CARD ID [" + user.getCardId() +"] is already in user")))
                            .switchIfEmpty(Mono.defer(() -> {
                                user.setPassword(passwordEncoder.encode(user.getPassword()));
                                return userRepository.save(user);
                            }));
                }));
    }

    private List<String> validateEntry(User user) {

        if (user == null)
            return Collections.singletonList("User is null");

        List<String> validationErrors = new ArrayList<>();

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            validationErrors.add("Email is required");
        } else if (!user.getEmail().matches(EMAIL_REGEX)) {
            validationErrors.add("Email format is invalid");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()
            || user.getName() == null || user.getName().isEmpty())
            validationErrors.add("Full name is required");
        if (user.getCardId() == null || user.getCardId().isEmpty())
            validationErrors.add("ID document is required");
        if (user.getBaseSalary() == null)
            validationErrors.add("Base salary is required");
        if (user.getPassword() == null || user.getPassword().isEmpty())
            validationErrors.add("Password is required");

        return validationErrors;
    }

    private boolean notValidBaseSalary(Double baseSalary) {
        return !(baseSalary >= 0 && baseSalary <= 15000000);
    }
}
