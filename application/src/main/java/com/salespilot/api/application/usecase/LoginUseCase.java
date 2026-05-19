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
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = tokenService.generateAccessToken(user);
        Instant refreshExpiresAt = Instant.now().plusSeconds(refreshTokenTtlSeconds);
        String refreshToken = refreshTokenRepository.create(user.getId(), refreshExpiresAt);

        return new AuthTokensDTO(accessToken, refreshToken, TOKEN_TYPE, tokenService.getAccessTokenTtlSeconds());
    }
}

