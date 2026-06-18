package com.salespilot.api.application.utils;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.exception.ForbiddenException;
import com.salespilot.api.domain.enums.CollaboratorRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MeetingAccessUtilsTest {

    private final UUID sellerId = UUID.randomUUID();
    private final UUID sellerCompanyId = UUID.randomUUID();

    @Test
    void grantAccess_systemAdmin_shouldPass() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        assertDoesNotThrow(() -> MeetingAccessUtils.grantAccess(sellerId, sellerCompanyId, authUser));
    }

    @Test
    void grantAccess_managerSameCompany_shouldPass() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), sellerCompanyId);

        assertDoesNotThrow(() -> MeetingAccessUtils.grantAccess(sellerId, sellerCompanyId, authUser));
    }

    @Test
    void grantAccess_managerDifferentCompany_shouldThrowForbidden() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), UUID.randomUUID());

        assertThrows(ForbiddenException.class, () -> MeetingAccessUtils.grantAccess(sellerId, sellerCompanyId, authUser));
    }

    @Test
    void grantAccess_sellerSelf_shouldPass() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, sellerId, UUID.randomUUID());

        assertDoesNotThrow(() -> MeetingAccessUtils.grantAccess(sellerId, sellerCompanyId, authUser));
    }

    @Test
    void grantAccess_sellerOther_shouldThrowForbidden() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, UUID.randomUUID(), UUID.randomUUID());

        assertThrows(ForbiddenException.class, () -> MeetingAccessUtils.grantAccess(sellerId, sellerCompanyId, authUser));
    }

    @Test
    void resolveCompanyScope_manager_returnsAuthUserCompanyId() {
        UUID companyId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), companyId);

        assertEquals(companyId, MeetingAccessUtils.resolveCompanyScope(authUser));
    }

    @Test
    void resolveCompanyScope_systemAdmin_returnsNull() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        assertNull(MeetingAccessUtils.resolveCompanyScope(authUser));
    }

    @Test
    void resolveCompanyScope_seller_returnsNull() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, UUID.randomUUID(), UUID.randomUUID());

        assertNull(MeetingAccessUtils.resolveCompanyScope(authUser));
    }

    @Test
    void resolveCollaboratorScope_seller_returnsAuthUserId() {
        UUID id = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, id, UUID.randomUUID());

        assertEquals(id, MeetingAccessUtils.resolveCollaboratorScope(authUser));
    }

    @Test
    void resolveCollaboratorScope_manager_returnsNull() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.MANAGER, UUID.randomUUID(), UUID.randomUUID());

        assertNull(MeetingAccessUtils.resolveCollaboratorScope(authUser));
    }

    @Test
    void resolveCollaboratorScope_systemAdmin_returnsNull() {
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        assertNull(MeetingAccessUtils.resolveCollaboratorScope(authUser));
    }
}