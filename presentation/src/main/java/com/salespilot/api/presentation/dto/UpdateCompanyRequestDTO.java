package com.salespilot.api.presentation.dto;

import com.salespilot.api.domain.enums.CompanyPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCompanyRequestDTO(
     @NotBlank String name,
     @NotNull CompanyPlan plan,
     @NotNull Boolean active
) {}

