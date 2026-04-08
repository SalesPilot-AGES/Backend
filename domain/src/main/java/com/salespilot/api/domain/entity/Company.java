package com.salespilot.api.domain.entity;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Company {
    private final UUID id;
    private final String taxId;
    private final Timestamp createdAt;
    private String name;
    private String plan;
    private boolean active;

    public void updateInfo(String name, String plan, boolean active) {
        this.name = name;
        this.plan = plan;
        this.active = active;
    }
}