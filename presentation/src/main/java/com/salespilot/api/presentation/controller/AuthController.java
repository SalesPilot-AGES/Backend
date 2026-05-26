package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.AuthTokensDTO;
import com.salespilot.api.application.usecase.LoginUseCase;
import com.salespilot.api.application.usecase.RefreshTokenUseCase;
import com.salespilot.api.presentation.dto.LoginRequest;
import com.salespilot.api.presentation.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<AuthTokensDTO> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.execute(request.email(), request.password()));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    @Transactional
    public ResponseEntity<AuthTokensDTO> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.execute(request.refreshToken()));
    }
}
