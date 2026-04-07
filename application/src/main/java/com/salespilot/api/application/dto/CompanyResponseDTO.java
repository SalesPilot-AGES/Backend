package com.salespilot.api.application.dto;

public record CompanyResponseDTO(String uuid, String nome, String cnpj, String plano, boolean is_active, String created_at) {}

