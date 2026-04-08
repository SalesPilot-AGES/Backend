package com.salespilot.api.application.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record CompanyResponseDTO(
    UUID id,
    String cnpj,
    Timestamp createdAt,
    String name,
    String plans,
    boolean isActive
) {}