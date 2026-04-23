package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Prompt {
    private UUID id;
    private UUID companyId;
    private String name;
    private String description;
    private String instructions;
    private boolean custom;
    private LocalDateTime createdAt;
}
