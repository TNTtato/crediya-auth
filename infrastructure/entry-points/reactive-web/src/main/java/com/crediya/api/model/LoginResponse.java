package com.crediya.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response Body")
public record LoginResponse(
        @Schema(description = "Token JWT de acceso", example = "eyB...")
        @JsonProperty("access")
        String accessToken
) {
}
