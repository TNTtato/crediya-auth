package com.crediya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response Body")
public record RegisterUserResponse(

        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        @JsonProperty("nombre_completo")
        String fullName,

        @Schema(description = "Identificador del rol asignado", example = "2")
        @JsonProperty("id_rol")
        Integer roleId
) {}
