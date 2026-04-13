package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.salespilot.api.domain.enums.CompanyPlan;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tax_id", nullable = false, unique = true)
    private String taxId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private CompanyPlan plan;

    @Column(name = "is_active", nullable = false)
    private boolean active;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = true, columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CollaboratorEntity> collaborators = new ArrayList<>();

    public CompanyEntity(String name, String taxId, CompanyPlan plan, boolean active) {
        this.name = name;
        this.taxId = taxId;
        this.plan = plan;
        this.active = active;
    }
}