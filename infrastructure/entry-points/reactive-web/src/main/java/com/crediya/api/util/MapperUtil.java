package com.crediya.api.util;

import com.crediya.api.model.RegisterUserRequest;
import com.crediya.model.usuario.Usuario;

public class MapperUtil {

    public static Usuario fromRequestToUserDomain(RegisterUserRequest registerUserRequest) {
        Usuario usuario = new Usuario();
        usuario.setNombre(registerUserRequest.name());
        usuario.setApellido(registerUserRequest.lastName());
        usuario.setEmail(registerUserRequest.email());
        usuario.setDocumentoIdentidad(registerUserRequest.idDocument());
        usuario.setIdRol(registerUserRequest.roleId());
        usuario.setTelefono(registerUserRequest.phone());
        usuario.setSalarioBase(registerUserRequest.baseSalary());

        return usuario;
    }
}
