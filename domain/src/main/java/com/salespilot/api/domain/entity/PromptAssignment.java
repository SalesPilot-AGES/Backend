package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PromptAssignment {
    private UUID id;
    private UUID promptId;
    private UUID collaboratorId;
    private LocalDateTime createdAt;
}
