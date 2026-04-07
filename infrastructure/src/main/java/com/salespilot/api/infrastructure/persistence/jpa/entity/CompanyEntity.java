package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import java.sql.Timestamp;
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

    @Column(name = "cnpj", unique = true, nullable = false)
    private String cnpj;

    // @Column(name = "created_at", updatable = false)
    // private Timestamp createdAt;

    @Column(name = "name", nullable = false)
    private String name;

    // @Column(name = "phone", nullable = false)
    // private String phone;

    // @Column(name = "address", nullable = false)
    // private String address;

    // @Column(name = "plans", nullable = false)
    // private String plans;

    // @Column(name = "isActive", nullable = false)
    // private boolean active;
}