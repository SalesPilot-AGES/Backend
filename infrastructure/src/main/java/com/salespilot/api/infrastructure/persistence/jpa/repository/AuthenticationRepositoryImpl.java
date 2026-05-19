package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.AuthUser;
import com.salespilot.api.domain.repository.AuthenticationRepository;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.AuthUserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthenticationRepositoryImpl implements AuthenticationRepository {
    private final CollaboratorJpaRepository collaboratorJpaRepository;
    private final AuthUserMapper mapper;

    public AuthenticationRepositoryImpl(CollaboratorJpaRepository collaboratorJpaRepository, AuthUserMapper mapper) {
        this.collaboratorJpaRepository = collaboratorJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return collaboratorJpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findById(UUID id) {
        return collaboratorJpaRepository.findById(id).map(mapper::toDomain);
    }
}

