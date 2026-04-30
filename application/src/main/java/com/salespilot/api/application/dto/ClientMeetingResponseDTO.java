package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.Client;

import java.util.UUID;

public record ClientMeetingResponseDTO(
        UUID id,
        String name,
        String clientCompanyName,
        String sector,
        Integer overallSentiment
) {
    public static ClientMeetingResponseDTO from(Client client) {
        return new ClientMeetingResponseDTO(
                client.getId(),
                client.getName(),
                client.getClientCompanyName(),
                client.getSector(),
                client.getOverallSentiment()
        );
    }
}
