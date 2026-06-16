package com.salespilot.api.application.dto;

import com.salespilot.api.domain.enums.CollaboratorRole;

import java.util.UUID;

public record AuthUserDTO(
        CollaboratorRole role,
        UUID id,
        UUID companyId
) {
}
