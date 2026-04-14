package com.salespilot.api.domain.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Company {
    private UUID id;
    private String name;
    private String taxId;
    private String status;
    private Integer maxSellers;
    private Integer maxManagers;
    private String notes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
