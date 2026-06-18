package com.salespilot.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@With
public class Company {
    private UUID id;
    private String name;
    private String taxId;
    private String phone;
    private String address;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String plan;
    private List<Collaborator> collaborators;
    private Long totalMeetings;
    private Long totalCollaborators;
    private Long totalManagers;
}
