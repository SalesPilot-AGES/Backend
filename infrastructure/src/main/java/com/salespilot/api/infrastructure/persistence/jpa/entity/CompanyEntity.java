package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    @Column(name = "uuid")
    private UUID id;
    private String nome;
    @Column(name = "cnpj", unique = true)
    private String cnpj;
    private String plano;
    @Column(name = "is_active")
    private boolean isActive;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public CompanyEntity(UUID id, String nome, String cnpj, String plano, boolean isActive) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.plano = plano;
        this.isActive = isActive;
    }
}