package com.crediya.api.model;

public record LoginRequest(
        String email,
        String password
) {
}
