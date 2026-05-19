package com.salespilot.api.presentation.dto;

import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para atualização de um colaborador")
public record CollaboratorUpdateRequestDTO(
        @Schema(description = "UUID da empresa à qual o colaborador pertence", example = "b1c2d3e4-f5a6-7890-2345-67890abcdef1")
        @NotNull UUID companyId,

        @Schema(description = "Nome completo do colaborador", example = "Gabriel Ribeiro")
        @NotBlank String name,

        @Schema(description = "E-mail do colaborador", example = "gabriel@digitalsales.com")
        @Email @NotBlank String email,

        @Schema(description = "Se o colaborador está ativo", example = "true")
        boolean active,

        @Schema(description = "Telefone do colaborador", example = "+55 (11) 98888-7777")
        String phone,

        @Schema(description = "Preferências de interface do colaborador")
        CollaboratorPreferences preferences
) {}
