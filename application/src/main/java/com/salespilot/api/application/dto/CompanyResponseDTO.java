package com.salespilot.api.application.dto;

import java.util.UUID;

public record CompanyResponseDTO(UUID id, String name, String taxId, String plan, boolean active, String createdAt) {}

