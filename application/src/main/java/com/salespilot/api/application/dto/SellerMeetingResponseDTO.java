package com.salespilot.api.application.dto;

import com.salespilot.api.domain.entity.Collaborator;

import java.util.UUID;

public record SellerMeetingResponseDTO(
        UUID id,
        String name,
        String email
) {
    public static SellerMeetingResponseDTO from(Collaborator seller) {
        return new SellerMeetingResponseDTO(
                seller.getId(),
                seller.getName(),
                seller.getEmail()
        );
    }
}

