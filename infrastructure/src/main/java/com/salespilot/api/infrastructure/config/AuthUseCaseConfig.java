package com.salespilot.api.infrastructure.config;

import com.salespilot.api.application.service.PasswordHasher;
import com.salespilot.api.application.service.TokenService;
import com.salespilot.api.application.usecase.LoginUseCase;
import com.salespilot.api.application.usecase.RefreshTokenUseCase;
import com.salespilot.api.domain.repository.AuthenticationRepository;
import com.salespilot.api.domain.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfig {
    @Bean
    public LoginUseCase loginUseCase(AuthenticationRepository authenticationRepository,
                                     RefreshTokenRepository refreshTokenRepository,
                                     PasswordHasher passwordHasher,
                                     TokenService tokenService,
                                     @Value("${app.security.jwt.refresh-ttl-seconds:2592000}") long refreshTokenTtlSeconds) {
        return new LoginUseCase(authenticationRepository, refreshTokenRepository, passwordHasher, tokenService, refreshTokenTtlSeconds);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(AuthenticationRepository authenticationRepository,
                                                   RefreshTokenRepository refreshTokenRepository,
                                                   TokenService tokenService,
                                                   @Value("${app.security.jwt.refresh-ttl-seconds:2592000}") long refreshTokenTtlSeconds) {
        return new RefreshTokenUseCase(authenticationRepository, refreshTokenRepository, tokenService, refreshTokenTtlSeconds);
    }
}

