package com.salespilot.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequestDTO(
    @NotBlank String name,
    String taxId,
    String status,
    Integer maxSellers,
    Integer maxManagers,
    String notes
) {}
