package com.salespilot.api.application.dto;

public record EditCompanyRequestDTO(
    String name,
    String plan,
    boolean active
) {}