package com.salespilot.api.application.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record EnterpriseResponseDTO(UUID uuid, String nome, String cnpj, String plano, Boolean is_active, Timestamp created_at) {}
