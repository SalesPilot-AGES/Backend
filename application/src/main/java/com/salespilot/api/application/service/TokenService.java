package com.salespilot.api.application.service;

import com.salespilot.api.domain.entity.AuthUser;

public interface TokenService {
    String generateAccessToken(AuthUser user);
    long getAccessTokenTtlSeconds();
}

