package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.Client;

import java.util.UUID;

public record ClientResponseDTO(
        UUID id,
        String name,
        String clientCompanyName
) {
    public static ClientResponseDTO from(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getClientCompanyName()
        );
    }
}
