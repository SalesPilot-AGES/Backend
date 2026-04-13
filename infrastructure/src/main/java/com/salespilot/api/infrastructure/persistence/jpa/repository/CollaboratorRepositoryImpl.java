package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CollaboratorMapper;

import java.util.UUID;

public class CollaboratorRepositoryImpl implements CollaboratorRepository {
    private final CollaboratorMapper mapper;
    private final CollaboratorJpaRepository collaboratorJpaRepository;

    public CollaboratorRepositoryImpl(CollaboratorMapper mapper, CollaboratorJpaRepository collaboratorJpaRepository) {
        this.mapper = mapper;
        this.collaboratorJpaRepository = collaboratorJpaRepository;
    }

    /*@Override
    public Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, CollaboratorPreferences preferences) {
        CollaboratorEntity collaboratorEntity = new CollaboratorEntity(companyId, name, email, role, active, preferences);
    }*/
}
