package com.salespilot.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResponseDTO(
        UUID id,
        String name,
        String taxId,
        String status,
        Integer maxSellers,
        Integer maxManagers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
