package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {

    @Id
    private UUID id;

    @Column(name = "tax_id", unique = true, nullable = false)
    private String taxId;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "plan", nullable = false)
    private String plan;

    @Column(name = "is_active", nullable = false)
    private boolean active;
}