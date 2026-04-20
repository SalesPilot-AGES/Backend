package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "companies", catalog = "salespilot")
@Getter
@Setter
@NoArgsConstructor
public class Companies implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    private Set<Collaborators> collaborators = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    private Set<CompanySubscriptions> companySubscriptions = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    private Set<Prompt> prompts = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    private Set<Clients> clients = new HashSet<>(0);
}
