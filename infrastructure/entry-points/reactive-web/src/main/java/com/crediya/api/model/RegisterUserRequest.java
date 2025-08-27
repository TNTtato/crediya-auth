package com.crediya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterUserRequest(
        @JsonProperty("nombre") String name,
        @JsonProperty("apellido") String lastName,
        String email,
        @JsonProperty("documento_identidad") String idDocument,
        @JsonProperty("telefono") String phone,
        @JsonProperty("id_rol") Integer roleId,
        @JsonProperty("salario_base") Double baseSalary
) {
}
