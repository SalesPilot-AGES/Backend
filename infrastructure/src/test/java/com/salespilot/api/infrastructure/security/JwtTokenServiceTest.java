package com.salespilot.api.infrastructure.security;

import com.salespilot.api.domain.entity.AuthUser;
import com.salespilot.api.domain.enums.CollaboratorRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    private JwtTokenService service;

    @BeforeEach
    void setUp() {
        service = new JwtTokenService(jwtEncoder, "api", 1800L);
    }

    @Test
    void shouldReturnTokenValueFromEncoder() {
        Jwt jwt = Jwt.withTokenValue("mocked-token")
                .header("alg", "HS256")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "user@test.com",
                null,
                CollaboratorRole.SELLER,
                true
        );

        String token = service.generateAccessToken(user);

        assertEquals("mocked-token", token);
    }

    @Test
    void shouldCallEncoderWithCorrectSubject() {
        Jwt jwt = Jwt.withTokenValue("mocked-token")
                .header("alg", "HS256")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "user@test.com",
                null,
                CollaboratorRole.SELLER,
                true
        );

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);

        service.generateAccessToken(user);

        verify(jwtEncoder).encode(captor.capture());
        assertEquals(user.getId().toString(), captor.getValue().getClaims().getSubject());
    }

    @Test
    void shouldReturnConfiguredTtl() {
        JwtTokenService customService = new JwtTokenService(jwtEncoder, "api", 1800L);

        assertEquals(1800L, customService.getAccessTokenTtlSeconds());
    }
}
