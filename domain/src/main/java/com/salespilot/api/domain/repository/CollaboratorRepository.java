package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

import java.util.Optional;
import java.util.UUID;

public interface CollaboratorRepository {
    Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, CollaboratorPreferences preferences);
    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
    Optional<Collaborator> getCollaboratorById(UUID id);
}
