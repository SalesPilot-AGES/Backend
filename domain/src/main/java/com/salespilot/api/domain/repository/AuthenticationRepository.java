package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.AuthUser;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticationRepository {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findById(UUID id);
}
