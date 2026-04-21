package com.salespilot.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de uma empresa")
public record UpdateCompanyRequestDTO(
        @Schema(description = "Razão social da empresa", example = "Digital Sales Ltda")
        @NotBlank String name,

        @Schema(description = "Plano de assinatura", example = "PRO", allowableValues = {"BASIC", "PRO", "ENTERPRISE"})
        @NotBlank String plan,

        @Schema(description = "Se a empresa está ativa", example = "true")
        @NotNull Boolean active
) {}
