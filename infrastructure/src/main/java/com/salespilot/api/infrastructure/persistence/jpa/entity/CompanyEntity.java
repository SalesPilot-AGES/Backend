package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    private UUID id;
    private String name;
    @Column(name = "tax_id")
    private String taxId;
    //precisa ver como vai pegar esse dado do plano
    private String plan;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "created_at")
    private Timestamp createdAt;
}