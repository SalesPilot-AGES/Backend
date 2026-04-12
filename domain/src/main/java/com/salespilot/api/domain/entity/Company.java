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
    private UUID id;
    private String name;
    private String taxId;
    private CompanyPlan plan;
    private boolean active;
    private LocalDateTime createdAt;
    private List<Collaborator> collaborators;
}