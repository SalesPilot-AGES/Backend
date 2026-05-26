package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.repository.RefreshTokenRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int REFRESH_TOKEN_BYTES = 32;

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    @Transactional
    @Override
    public String create(UUID userId, Instant expiresAt) {
        byte[] tokenBytes = new byte[REFRESH_TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(tokenBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        String tokenHash = hashToken(rawToken);

        refreshTokenJpaRepository.save(new RefreshTokenEntity(userId, tokenHash, expiresAt));
        return rawToken;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UUID> findValidUserId(String rawToken, Instant now) {
        String tokenHash = hashToken(rawToken);
        return refreshTokenJpaRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .filter(token -> token.getExpiresAt().isAfter(now))
                .map(RefreshTokenEntity::getUserId);
    }

    @Transactional
    @Override
    public boolean revoke(String rawToken, Instant now) {
        String tokenHash = hashToken(rawToken);
        return refreshTokenJpaRepository.revokeByTokenHash(tokenHash, now) > 0;
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }
}
