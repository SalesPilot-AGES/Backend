package com.salespilot.api.presentation.dto;

import java.util.UUID;

public record EditCollaboratorRequestDTO(
    UUID companyId,
    String name,
    String email,
    boolean active,
    String preferences
) {}
