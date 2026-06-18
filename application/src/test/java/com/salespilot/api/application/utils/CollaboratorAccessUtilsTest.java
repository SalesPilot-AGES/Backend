package com.salespilot.api.application.utils;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.exception.ForbiddenException;
import com.salespilot.api.domain.enums.CollaboratorRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CollaboratorAccessUtilsTest {

    @Test
    void grantAccess_systemAdmin_shouldPass() {
        UUID companyId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        assertDoesNotThrow(() -> CollaboratorAccessUtils.grantAccess(companyId, authUser));
    }

    @Test
    void grantAccess_managerSameCompany_shouldPass() {
        UUID companyId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), companyId);

        assertDoesNotThrow(() -> CollaboratorAccessUtils.grantAccess(companyId, authUser));
    }

    @Test
    void grantAccess_managerDifferentCompany_shouldThrowForbidden() {
        UUID companyId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), UUID.randomUUID());

        assertThrows(ForbiddenException.class, () -> CollaboratorAccessUtils.grantAccess(companyId, authUser));
    }

    @Test
    void grantAccess_seller_shouldThrowForbidden() {
        UUID companyId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, UUID.randomUUID(), UUID.randomUUID());

        assertThrows(ForbiddenException.class, () -> CollaboratorAccessUtils.grantAccess(companyId, authUser));
    }

    @Test
    void grantSelfOrAdminAccess_systemAdmin_shouldPass() {
        UUID collaboratorId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        assertDoesNotThrow(() -> CollaboratorAccessUtils.grantSelfOrAdminAccess(collaboratorId, authUser));
    }

    @Test
    void grantSelfOrAdminAccess_selfSeller_shouldPass() {
        UUID collaboratorId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, collaboratorId, UUID.randomUUID());

        assertDoesNotThrow(() -> CollaboratorAccessUtils.grantSelfOrAdminAccess(collaboratorId, authUser));
    }

    @Test
    void grantSelfOrAdminAccess_otherSeller_shouldThrowForbidden() {
        UUID collaboratorId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, UUID.randomUUID(), UUID.randomUUID());

        assertThrows(ForbiddenException.class, () -> CollaboratorAccessUtils.grantSelfOrAdminAccess(collaboratorId, authUser));
    }
}