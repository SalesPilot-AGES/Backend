package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.salespilot.api.domain.enums.CompanyPlan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Company {
    private final UUID id;
    private final String name;
    private final String taxId;
    private final CompanyPlan plan;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final List<Collaborator> collaborators;
}