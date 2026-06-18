package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthTokensDTO;
import com.salespilot.api.application.exception.InvalidCredentialsException;
import com.salespilot.api.application.service.PasswordHasher;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationRepository authRepo;

    @Mock
    private RefreshTokenRepository refreshTokenRepo;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenService tokenService;

    private LoginUseCase useCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID companyId = UUID.randomUUID();
    private final String email = "user@example.com";
    private final String rawPw = "secret";
    private final String passwordHash = "$2a$10$hashedpassword";

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCase(authRepo, refreshTokenRepo, passwordHasher, tokenService, 3600L);
    }

    @Test
    void shouldReturnTokensForValidCredentials() {
        AuthUser activeUser = new AuthUser(userId, companyId, email, passwordHash, CollaboratorRole.SELLER, true);

        when(authRepo.findByEmail(email)).thenReturn(Optional.of(activeUser));
        when(passwordHasher.matches(rawPw, passwordHash)).thenReturn(true);
        when(tokenService.generateAccessToken(activeUser)).thenReturn("access-token");
        when(tokenService.getAccessTokenTtlSeconds()).thenReturn(3600L);
        when(refreshTokenRepo.create(any(), any())).thenReturn("refresh-token");

        AuthTokensDTO result = useCase.execute(email, rawPw);

        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(3600L, result.expiresInSeconds());
    }

    @Test
    void shouldThrowInvalidCredentialsWhenUserNotFound() {
        when(authRepo.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordHasher.matches(any(), any())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(email, rawPw));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenUserIsInactive() {
        AuthUser inactiveUser = new AuthUser(userId, companyId, email, passwordHash, CollaboratorRole.SELLER, false);

        when(authRepo.findByEmail(email)).thenReturn(Optional.of(inactiveUser));
        when(passwordHasher.matches(any(), any())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(email, rawPw));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenPasswordIsWrong() {
        AuthUser activeUser = new AuthUser(userId, companyId, email, passwordHash, CollaboratorRole.SELLER, true);

        when(authRepo.findByEmail(email)).thenReturn(Optional.of(activeUser));
        when(passwordHasher.matches(rawPw, passwordHash)).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(email, rawPw));
    }

    @Test
    void shouldCallDummyHashWhenUserNotFound() {
        when(authRepo.findByEmail(email)).thenReturn(Optional.empty());
        lenient().when(passwordHasher.matches(any(), any())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(email, rawPw));

        verify(passwordHasher, times(1)).matches(eq(rawPw), any(String.class));
    }
}
