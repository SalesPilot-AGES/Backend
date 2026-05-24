package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorStatusHistoryEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CollaboratorMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.CollaboratorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CollaboratorRepositoryImpl implements CollaboratorRepository {
    private final CollaboratorMapper mapper;
    private final CollaboratorJpaRepository collaboratorJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final CollaboratorStatusHistoryJpaRepository collaboratorStatusHistoryJpaRepository;

    public CollaboratorRepositoryImpl(
            CollaboratorMapper mapper,
            CollaboratorJpaRepository collaboratorJpaRepository,
            CompanyJpaRepository companyJpaRepository,
            CollaboratorStatusHistoryJpaRepository collaboratorStatusHistoryJpaRepository
    ) {
        this.mapper = mapper;
        this.collaboratorJpaRepository = collaboratorJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
        this.collaboratorStatusHistoryJpaRepository = collaboratorStatusHistoryJpaRepository;
    }

    @Transactional
    @Override
    public Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, String phone, CollaboratorPreferences preferences) {
        CompanyEntity companyEntity = companyJpaRepository.getReferenceById(companyId);

        CollaboratorEntity collaboratorEntity = new CollaboratorEntity(companyEntity, name, email, role, active, phone, preferences);
        CollaboratorEntity savedEntity = collaboratorJpaRepository.save(collaboratorEntity);
        collaboratorStatusHistoryJpaRepository.save(new CollaboratorStatusHistoryEntity(savedEntity, active));

        return mapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByCompanyIdAndEmail(UUID companyId, String email) {
        return collaboratorJpaRepository.existsByCompanyIdAndEmail(companyId, email);
    }

    @Transactional
    @Override
    public Collaborator update(UUID companyId, UUID collaboratorId, String name, String email, String phone, boolean active, CollaboratorPreferences preferences) {
        CollaboratorEntity collaboratorEntity = collaboratorJpaRepository
            .findById(collaboratorId)
            .orElseThrow(() -> new CollaboratorNotFoundException(collaboratorId));

        CompanyEntity companyEntity = companyJpaRepository.getReferenceById(companyId);

        boolean previousActive = collaboratorEntity.isActive();
        collaboratorEntity.setCompany(companyEntity);
        collaboratorEntity.setName(name);
        collaboratorEntity.setEmail(email);
        collaboratorEntity.setPhone(phone);
        collaboratorEntity.setActive(active);
        collaboratorEntity.setPreferences(preferences);

        CollaboratorEntity saved = collaboratorJpaRepository.save(collaboratorEntity);
        if (previousActive != active) {
            collaboratorStatusHistoryJpaRepository.save(new CollaboratorStatusHistoryEntity(saved, active));
        }
        return mapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Collaborator> getCollaboratorById(UUID id) {
        return collaboratorJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Collaborator> getManagers(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        Specification<CollaboratorEntity> spec = Specification
                .where(CollaboratorSpecification.roleEquals(CollaboratorRole.MANAGER))
                .and(CollaboratorSpecification.nameLike(name))
                .and(CollaboratorSpecification.emailEquals(email))
                .and(CollaboratorSpecification.companyIdEquals(companyId))
                .and(CollaboratorSpecification.isActiveEquals(active));

        return collaboratorJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Collaborator> getSellers(String name, String email, UUID companyId, Boolean active, Pageable pageable) {
        Specification<CollaboratorEntity> spec = Specification
                .where(CollaboratorSpecification.roleEquals(CollaboratorRole.SELLER))
                .and(CollaboratorSpecification.nameLike(name))
                .and(CollaboratorSpecification.emailEquals(email))
                .and(CollaboratorSpecification.companyIdEquals(companyId))
                .and(CollaboratorSpecification.isActiveEquals(active));

        return collaboratorJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countAllActiveSellersByPeriod(LocalDateTime period) {
        return collaboratorStatusHistoryJpaRepository.countActiveByRoleSnapshotAt(CollaboratorRole.SELLER.name(), period);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countAllActiveSellers() {
        Specification<CollaboratorEntity> spec = Specification
            .where(CollaboratorSpecification.roleEquals(CollaboratorRole.SELLER))
            .and(CollaboratorSpecification.isActiveEquals(true));
        return collaboratorJpaRepository.count(spec);
    }

    @Transactional
    @Override
    public void updatePasswordHash(UUID collaboratorId, String passwordHash) {
        CollaboratorEntity collaboratorEntity = collaboratorJpaRepository
            .findById(collaboratorId)
            .orElseThrow(() -> new CollaboratorNotFoundException(collaboratorId));

        collaboratorEntity.setPasswordHash(passwordHash);
        collaboratorJpaRepository.save(collaboratorEntity);
    }
}
