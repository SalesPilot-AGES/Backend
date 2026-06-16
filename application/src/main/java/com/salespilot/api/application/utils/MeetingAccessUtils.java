package com.salespilot.api.application.utils;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.exception.ForbiddenException;
import com.salespilot.api.domain.enums.CollaboratorRole;

import java.util.UUID;

public class MeetingAccessUtils {
    private MeetingAccessUtils() {}

    public static void grantAccess(UUID sellerId, UUID sellerCompanyId, AuthUserDTO authUser) {
        CollaboratorRole authUserRole = authUser.role();

        if(authUserRole == CollaboratorRole.SYSTEM_ADMIN){
            return;
        }
        if(authUserRole == CollaboratorRole.MANAGER && authUser.companyId().equals(sellerCompanyId)){
            return;
        }
        if(authUserRole == CollaboratorRole.SELLER && authUser.id().equals(sellerId)){
            return;
        }

        throw new ForbiddenException();
    }

    public static UUID resolveCompanyScope(AuthUserDTO authUser) {
        if(authUser.role() == CollaboratorRole.MANAGER) {
            return authUser.companyId();
        }

        return null;
    }

    public static UUID resolveCollaboratorScope(AuthUserDTO authUser) {
        if(authUser.role() == CollaboratorRole.SELLER) {
            return authUser.id();
        }

        return null;
    }
}
