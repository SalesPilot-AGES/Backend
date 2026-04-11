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
    @Column(name = "id")
    private UUID id;
    private String name;
    @Column(name = "tax_id", unique = true)
    private String taxId;
    private String plan;
    @Column(name = "is_active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public CompanyEntity(UUID id, String name, String taxId, String plan, boolean active) {
        this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.plan = plan;
        this.active = active;
    }
}