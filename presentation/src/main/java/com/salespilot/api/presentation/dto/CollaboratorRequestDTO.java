package com.salespilot.api.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CollaboratorRequestDTO(
        @NotBlank
        UUID companyId,

        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        @NotNull
        boolean active,

        @Valid
        @NotBlank
        CollaboratorPreferencesDTO collaboratorPreferencesDTO
) {}
