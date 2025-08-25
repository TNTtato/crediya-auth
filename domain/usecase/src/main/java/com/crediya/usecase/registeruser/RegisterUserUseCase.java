package com.crediya.usecase.registeruser;

import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> execute(Usuario usuario) {

        List<String> validationErrors = validateEntry(usuario);
        if (!validationErrors.isEmpty()) {
            return Mono.error(new UserValidationException(validationErrors.toString()));
        }

        return usuarioRepository.findByEmail(usuario.getEmail())
                .flatMap(exists -> Mono.<Usuario>error(new EmailAlreadyInUseException("Email already in use")))
                .switchIfEmpty(Mono.defer(() -> {
                    if (notValidBaseSalary(usuario.getSalarioBase()))
                        throw new NotValidBaseSalaryException("Base salary not in range");

                    return usuarioRepository.save(usuario);
                }));
    }

    private List<String> validateEntry(Usuario usuario) {

        if (usuario == null)
            return Collections.singletonList("User is null");

        List<String> validationErrors = new ArrayList<>();

        if (usuario.getEmail() == null || usuario.getEmail().isEmpty())
            validationErrors.add("Email is required");
        if (usuario.getApellido() == null || usuario.getApellido().isEmpty()
            || usuario.getNombre() == null || usuario.getNombre().isEmpty())
            validationErrors.add("Full name is required");
        if (usuario.getDocumentoIdentidad() == null || usuario.getDocumentoIdentidad().isEmpty())
            validationErrors.add("ID document is required");
        if (usuario.getSalarioBase() == null)
            validationErrors.add("Base salary is required");

        return validationErrors;
    }

    private boolean notValidBaseSalary(Double baseSalary) {
        return !(baseSalary >= 0 && baseSalary <= 15000000);
    }
}
