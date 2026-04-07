package com.salespilot.api.domain.entity;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Company {
    private final UUID id;
    private final String cnpj;
    private final Timestamp createdAt;
    private String name;
    private String plans;
    private boolean active;

    public void updateInfo(String name, String plans, boolean active) {
        this.name = name;
        this.plans = plans;
        this.active = active;
    }
}