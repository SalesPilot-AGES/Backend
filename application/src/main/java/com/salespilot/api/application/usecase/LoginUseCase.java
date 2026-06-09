package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthTokensDTO;
import com.salespilot.api.application.exception.InvalidCredentialsException;
import com.salespilot.api.application.service.PasswordHasher;
import com.salespilot.api.application.service.TokenService;
import com.salespilot.api.domain.entity.AuthUser;
import com.salespilot.api.domain.repository.AuthenticationRepository;
import com.salespilot.api.domain.repository.RefreshTokenRepository;

import java.time.Instant;

public class LoginUseCase {
    private final AuthenticationRepository authenticationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final long refreshTokenTtlSeconds;

    private static final String TOKEN_TYPE = "Bearer";
    private static final String DUMMY_UNKNOWN_OR_INACTIVE_HASH = "$2a$10$7g0zK3tT8S.0dG4f0ZxR.u4uB8V5ZtX4H2xB1O1mXw9JQ2o7yP7i6";

    public LoginUseCase(
        AuthenticationRepository authenticationRepository,
        RefreshTokenRepository refreshTokenRepository,
        PasswordHasher passwordHasher,
        TokenService tokenService,
        long refreshTokenTtlSeconds
    ) {
        this.authenticationRepository = authenticationRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordHasher = passwordHasher;
        this.tokenService = tokenService;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    }

    public AuthTokensDTO execute(String email, String rawPassword) {
        AuthUser user = authenticationRepository.findByEmail(email)
                .filter(AuthUser::isActive)
                .orElse(null);

        if (user == null) {
            // dummy hash check to reduce timing differences for unknown/inactive users
            passwordHasher.matches(rawPassword, DUMMY_UNKNOWN_OR_INACTIVE_HASH);
            throw new InvalidCredentialsException();
        }

        if (!passwordHasher.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = tokenService.generateAccessToken(user);
        Instant refreshExpiresAt = Instant.now().plusSeconds(refreshTokenTtlSeconds);
        String refreshToken = refreshTokenRepository.create(user.getId(), refreshExpiresAt);

        return new AuthTokensDTO(accessToken, refreshToken, TOKEN_TYPE, tokenService.getAccessTokenTtlSeconds());
    }
}
