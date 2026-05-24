package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthTokensDTO;
import com.salespilot.api.application.exception.RefreshTokenInvalidException;
import com.salespilot.api.application.service.TokenService;
import com.salespilot.api.domain.entity.AuthUser;
import com.salespilot.api.domain.repository.AuthenticationRepository;
import com.salespilot.api.domain.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

public class RefreshTokenUseCase {
    private final AuthenticationRepository authenticationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final long refreshTokenTtlSeconds;

    private static final String TOKEN_TYPE = "Bearer";

    public RefreshTokenUseCase(
       AuthenticationRepository authenticationRepository,
       RefreshTokenRepository refreshTokenRepository,
       TokenService tokenService,
       long refreshTokenTtlSeconds
    ) {
        this.authenticationRepository = authenticationRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    }

    public AuthTokensDTO execute(String refreshToken) {
        Instant now = Instant.now();
        UUID userId = refreshTokenRepository.findValidUserId(refreshToken, now)
                .orElseThrow(RefreshTokenInvalidException::new);

        AuthUser user = authenticationRepository.findById(userId)
                .filter(AuthUser::isActive)
                .orElseThrow(RefreshTokenInvalidException::new);

        if (!refreshTokenRepository.revoke(refreshToken, now)) {
            throw new RefreshTokenInvalidException();
        }

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = refreshTokenRepository.create(user.getId(), now.plusSeconds(refreshTokenTtlSeconds));

        return new AuthTokensDTO(newAccessToken, newRefreshToken, TOKEN_TYPE, tokenService.getAccessTokenTtlSeconds());
    }
}
