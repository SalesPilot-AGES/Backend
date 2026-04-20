package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "collaborators", catalog = "salespilot")
@Getter
@Setter
@NoArgsConstructor
public class Collaborators implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Companies companies;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private CollaboratorRole role;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "active", nullable = false)
    private boolean active;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "json")
    private String preferences;

    @Column(name = "average_feeling")
    private Integer averageFeeling;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collaborators")
    private Set<Clients> clients = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collaborators")
    private Set<Meetings> meetings = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collaborators")
    private Set<PromptAssignments> promptAssignments = new HashSet<>(0);
}
