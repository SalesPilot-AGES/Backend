package com.salespilot.api.presentation.dto;

import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CollaboratorRequestDTO(
        @NotNull
        UUID companyId,

        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        boolean active,

        @Valid
        @NotNull
        CollaboratorPreferences preferences
) {}