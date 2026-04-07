package com.salespilot.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyRequestDTO(
    @NotBlank String nome, 
    @NotBlank String cnpj, 
    @NotBlank String plano, 
    @NotNull Boolean is_active
) {}
