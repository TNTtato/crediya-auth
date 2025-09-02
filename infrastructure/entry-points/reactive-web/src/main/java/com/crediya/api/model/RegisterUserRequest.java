package com.crediya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to register a new user")
public record RegisterUserRequest(

        @Schema(description = "Nombre del usuario", example = "Juan")
        @JsonProperty("nombre")
        String name,

        @Schema(description = "Apellido del usuario", example = "Pérez")
        @JsonProperty("apellido")
        String lastName,

        @Schema(description = "Correo electrónico único", example = "juan.perez@example.com")
        String email,

        @Schema(description = "Contraseña del usuario", example = "*******")
        @JsonProperty("password")
        String password,

        @Schema(description = "Documento de identidad", example = "1020304050")
        @JsonProperty("documento_identidad")
        String cardId,

        @Schema(description = "Número de teléfono", example = "+57 3001234567")
        @JsonProperty("telefono")
        String phone,

        @Schema(description = "Identificador del rol asignado", example = "2")
        @JsonProperty("id_rol")
        Integer roleId,

        @Schema(description = "Salario base en COP", example = "2500000")
        @JsonProperty("salario_base")
        Double baseSalary
) {}
