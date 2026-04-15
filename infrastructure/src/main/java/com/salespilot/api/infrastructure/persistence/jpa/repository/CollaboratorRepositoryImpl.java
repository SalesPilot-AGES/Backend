package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CollaboratorMapper;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CollaboratorRepositoryImpl implements CollaboratorRepository {
    private final CollaboratorMapper mapper;
    private final CollaboratorJpaRepository collaboratorJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;

    public CollaboratorRepositoryImpl(CollaboratorMapper mapper, CollaboratorJpaRepository collaboratorJpaRepository, CompanyJpaRepository companyJpaRepository) {
        this.mapper = mapper;
        this.collaboratorJpaRepository = collaboratorJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, CollaboratorPreferences preferences) {
        CompanyEntity companyEntity = companyJpaRepository.getReferenceById(companyId);

        CollaboratorEntity collaboratorEntity = new CollaboratorEntity(companyEntity, name, email, role, active, preferences);
        CollaboratorEntity savedEntity = collaboratorJpaRepository.save(collaboratorEntity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByCompanyIdAndEmail(UUID companyId, String email) {
        return collaboratorJpaRepository.existsByCompanyIdAndEmail(companyId, email);
    }

    @Override
    public Collaborator update(UUID companyId, UUID collaboratorId, String name, String email, boolean active, CollaboratorPreferences preferences) {
        CollaboratorEntity collaboratorEntity = collaboratorJpaRepository
            .findByIdAndCompany_Id(collaboratorId, companyId)
            .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        if (!collaboratorEntity.getEmail().equals(email) &&
            collaboratorJpaRepository.existsByCompanyIdAndEmail(companyId, email)) {
            throw new RuntimeException("Email already in use");
        }
        
        collaboratorEntity.setName(name);
        collaboratorEntity.setEmail(email);
        collaboratorEntity.setActive(active);
        collaboratorEntity.setPreferences(preferences);

        CollaboratorEntity saved = collaboratorJpaRepository.save(collaboratorEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByCollaboratorId(UUID collaboratorId) {
        return collaboratorJpaRepository.existsById(collaboratorId);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Optional<Collaborator> getCollaboratorById(UUID id) {
        return collaboratorJpaRepository.findById(id).map(mapper::toDomain);
    }
}
