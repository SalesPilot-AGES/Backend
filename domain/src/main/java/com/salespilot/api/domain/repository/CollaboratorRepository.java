package com.salespilot.api.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

public interface CollaboratorRepository {
    Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, CollaboratorPreferences preferences);
    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
    Page<Collaborator> getManagers(String name, String email, UUID companyId, Boolean active, Pageable pageable);
}

