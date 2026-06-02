package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.AuthUser;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthUserMapper {
    public AuthUser toDomain(CollaboratorEntity collaboratorEntity) {
        return new AuthUser(
                collaboratorEntity.getId(),
                collaboratorEntity.getCompany().getId(),
                collaboratorEntity.getEmail(),
                collaboratorEntity.getPasswordHash(),
                collaboratorEntity.getRole(),
                collaboratorEntity.isActive()
        );
    }
}

