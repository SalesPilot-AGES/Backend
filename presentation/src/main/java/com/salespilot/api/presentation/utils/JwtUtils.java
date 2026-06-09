package com.salespilot.api.presentation.utils;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.domain.enums.CollaboratorRole;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class JwtUtils {
    private JwtUtils() {}

    public static AuthUserDTO toAuthUserDTO(Jwt jwt) {
        String companyIdStr = jwt.getClaimAsString("company_id");

        return new AuthUserDTO(
                CollaboratorRole.valueOf(jwt.getClaimAsString("role")),
                UUID.fromString(jwt.getClaimAsString("sub")),
                companyIdStr != null ? UUID.fromString(companyIdStr) : null
        );
    }
}
