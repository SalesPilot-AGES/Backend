package com.salespilot.api.application.dto;

import java.util.UUID;

public record CompanyResponseDTO(UUID uuid, String nome, String cnpj, String plano, boolean isActive, String createdAt) {}

