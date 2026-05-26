package com.salespilot.api.application.utils;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.exception.ForbiddenException;
import com.salespilot.api.domain.enums.CollaboratorRole;

import java.util.UUID;

public class CollaboratorAccessUtils {
    private CollaboratorAccessUtils() {}

    public static void grantAccess(UUID companyId, AuthUserDTO authUser) {
        CollaboratorRole authUserRole = authUser.role();

        if(authUserRole == CollaboratorRole.SYSTEM_ADMIN) {
            return;
        }
        if(authUserRole == CollaboratorRole.MANAGER) {
            if(authUser.companyId().equals(companyId)) {
                return;
            }
        }

        throw new ForbiddenException();
    }

    public static void grantSelfOrAdminAccess(UUID collaboratorId, AuthUserDTO authUser) {
        if(authUser.role() == CollaboratorRole.SYSTEM_ADMIN) {
            return;
        }

        if(authUser.id().equals(collaboratorId)) {
            return;
        }

        throw new ForbiddenException();
    }
}
