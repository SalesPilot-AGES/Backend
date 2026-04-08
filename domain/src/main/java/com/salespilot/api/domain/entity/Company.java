package com.salespilot.api.domain.entity;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Company {
    private final UUID id;
    private final String name;
    private final String taxId;
    private final String plan;
    private final boolean active;
    private final Timestamp createdAt;
}
