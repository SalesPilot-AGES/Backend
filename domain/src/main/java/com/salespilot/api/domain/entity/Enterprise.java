package com.salespilot.api.domain.entity;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Enterprise {
    private final UUID id;
    private final String nome;
    private final String cnpj;
    private final String plano;
    private final boolean is_active;
    private final Timestamp created_at;
}
