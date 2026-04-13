package com.salespilot.api.domain.repository;

import java.util.UUID;

import com.salespilot.api.domain.entity.Collaborator;

public interface CollaboratorRepository {
    Collaborator editCollaborator(UUID companyId, String name, String email, boolean active, String preferences);
}
