package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class CompanyEntity {

    @Id
    private UUID id;
    private String nome;
    private String cnpj;
    private String plano;
    private Boolean isActive;
    private Timestamp created_at;
}