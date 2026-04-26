package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CollaboratorRepository {
    Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, String phone, CollaboratorPreferences preferences);
    Collaborator update(UUID companyId, UUID collaboratorId, String name, String email, boolean active, CollaboratorPreferences preferences);
    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
    Optional<Collaborator> getCollaboratorById(UUID id);
    Page<Collaborator> getManagers(String name, String email, UUID companyId, Boolean active, Pageable pageable);
}
