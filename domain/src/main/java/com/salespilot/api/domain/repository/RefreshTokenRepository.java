package com.salespilot.api.domain.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    String create(UUID userId, Instant expiresAt);
    Optional<UUID> findValidUserId(String rawToken, Instant now);
    void revoke(String rawToken, Instant now);
}

