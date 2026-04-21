package com.salespilot.api.domain.entity;

import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Collaborator {
    private UUID id;

    private UUID companyId;

    private String name;

    private String email;

    private CollaboratorRole role;

    private boolean active;

    private CollaboratorPreferences preferences;

    private LocalDateTime createdAt;
}