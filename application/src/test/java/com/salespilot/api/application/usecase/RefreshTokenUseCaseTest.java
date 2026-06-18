package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthTokensDTO;
import com.salespilot.api.application.exception.RefreshTokenInvalidException;
import com.salespilot.api.application.service.TokenService;
import com.salespilot.api.domain.entity.AuthUser;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.AuthenticationRepository;
import com.salespilot.api.domain.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private AuthenticationRepository authRepo;

    @Mock
    private RefreshTokenRepository refreshTokenRepo;

    @Mock
    private TokenService tokenService;

    private RefreshTokenUseCase useCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID companyId = UUID.randomUUID();
    private final AuthUser activeUser = new AuthUser(userId, companyId, "user@test.com", "hash", CollaboratorRole.SELLER, true);
    private final AuthUser inactiveUser = new AuthUser(userId, companyId, "user@test.com", "hash", CollaboratorRole.SELLER, false);

    @BeforeEach
    void setUp() {
        useCase = new RefreshTokenUseCase(authRepo, refreshTokenRepo, tokenService, 3600L);
    }

    @Test
    void shouldReturnNewTokensForValidRefreshToken() {
        when(refreshTokenRepo.findValidUserId(eq("old-token"), any(Instant.class))).thenReturn(Optional.of(userId));
        when(authRepo.findById(userId)).thenReturn(Optional.of(activeUser));
        when(refreshTokenRepo.revoke(eq("old-token"), any(Instant.class))).thenReturn(true);
        when(tokenService.generateAccessToken(activeUser)).thenReturn("new-access-token");
        when(tokenService.getAccessTokenTtlSeconds()).thenReturn(3600L);
        when(refreshTokenRepo.create(eq(userId), any(Instant.class))).thenReturn("new-refresh-token");

        AuthTokensDTO result = useCase.execute("old-token");

        assertEquals("new-access-token", result.accessToken());
        assertEquals("new-refresh-token", result.refreshToken());
        assertEquals("Bearer", result.tokenType());
        verify(refreshTokenRepo, times(1)).revoke(eq("old-token"), any(Instant.class));
    }

    @Test
    void shouldThrowWhenRefreshTokenNotFound() {
        when(refreshTokenRepo.findValidUserId(any(), any(Instant.class))).thenReturn(Optional.empty());

        assertThrows(RefreshTokenInvalidException.class, () -> useCase.execute("invalid-token"));

        verify(authRepo, never()).findById(any());
    }

    @Test
    void shouldThrowWhenUserIsInactive() {
        when(refreshTokenRepo.findValidUserId(any(), any(Instant.class))).thenReturn(Optional.of(userId));
        when(authRepo.findById(userId)).thenReturn(Optional.of(inactiveUser));

        assertThrows(RefreshTokenInvalidException.class, () -> useCase.execute("old-token"));

        verify(refreshTokenRepo, never()).revoke(any(), any(Instant.class));
    }

    @Test
    void shouldThrowWhenRevokeReturnsFalse() {
        when(refreshTokenRepo.findValidUserId(any(), any(Instant.class))).thenReturn(Optional.of(userId));
        when(authRepo.findById(userId)).thenReturn(Optional.of(activeUser));
        when(refreshTokenRepo.revoke(any(), any(Instant.class))).thenReturn(false);

        assertThrows(RefreshTokenInvalidException.class, () -> useCase.execute("old-token"));

        verify(tokenService, never()).generateAccessToken(any());
    }
}
