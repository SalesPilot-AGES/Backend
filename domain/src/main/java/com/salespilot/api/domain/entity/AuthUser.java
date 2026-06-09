package com.salespilot.api.domain.entity;

import com.salespilot.api.domain.enums.CollaboratorRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthUser {
    private UUID id;
    private UUID companyId;
    private String email;
    private String passwordHash;
    private CollaboratorRole role;
    private boolean active;
}

