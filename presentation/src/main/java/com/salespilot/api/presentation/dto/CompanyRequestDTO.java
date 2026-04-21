package com.salespilot.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cadastro de uma empresa")
public record CompanyRequestDTO(
        @Schema(description = "Razão social da empresa", example = "Digital Sales Ltda")
        @NotBlank String name,

        @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-90")
        @NotBlank String taxId,

        @Schema(description = "Plano de assinatura", example = "BASIC", allowableValues = {"BASIC", "PRO", "ENTERPRISE"})
        @NotBlank String plan,

        @Schema(description = "Se a empresa está ativa", example = "true")
        @NotNull Boolean active
) {}
