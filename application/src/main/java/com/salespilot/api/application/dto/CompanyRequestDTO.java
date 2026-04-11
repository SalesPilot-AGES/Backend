package com.salespilot.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record CompanyRequestDTO(
    @NotBlank String name, 
    @NotBlank String taxId, 
    @NotBlank String plan, 
    @NotNull Boolean active
) {}
