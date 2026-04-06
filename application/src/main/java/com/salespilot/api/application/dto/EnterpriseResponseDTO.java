package com.salespilot.api.application.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record EnterpriseResponseDTO(UUID uuid, String nome, String cnpj, String plano, Boolean isActive, Timestamp created_at) {}
