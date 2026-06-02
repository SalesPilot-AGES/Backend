package com.salespilot.api.application.dto;

public record AuthTokensDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds
) {
}

