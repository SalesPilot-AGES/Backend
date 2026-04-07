package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    @Column(name = "uuid")
    private UUID id;
    private String nome;
    private String cnpj;
    private String plano;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "created_at")
    private Timestamp createdAt;

    public CompanyEntity() {

    }
}