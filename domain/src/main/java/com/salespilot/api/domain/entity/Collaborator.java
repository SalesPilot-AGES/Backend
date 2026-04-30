package com.salespilot.api.domain.entity;

import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Getter
@AllArgsConstructor
public class Collaborator {
    private UUID id;

    private UUID companyId;

    private String name;

    private String email;

    private String phone;

    private CollaboratorRole role;

    private boolean active;

    private Integer averageFeeling;

    private Integer totalMeetings;

    private List<Meeting> meetings;
    
    private CollaboratorPreferences preferences;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Collaborator(UUID id, UUID companyId, String name, String email, String phone, CollaboratorRole role, boolean active, Integer averageFeeling, CollaboratorPreferences preferences, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.active = active;
        this.averageFeeling = averageFeeling;
        this.preferences = preferences;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}