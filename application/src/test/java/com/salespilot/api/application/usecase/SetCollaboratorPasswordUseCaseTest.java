package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.ForbiddenException;
import com.salespilot.api.application.service.PasswordHasher;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetCollaboratorPasswordUseCaseTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private SetCollaboratorPasswordUseCase useCase;

    private Collaborator buildCollaborator(UUID id) {
        return new Collaborator(id, UUID.randomUUID(), "Test", "test@test.com", null,
                CollaboratorRole.SELLER, true, null, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void shouldUpdatePasswordWhenAdminSetsAnyPassword() {
        UUID collaboratorId = UUID.randomUUID();
        Collaborator collaborator = buildCollaborator(collaboratorId);
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(collaborator));
        when(passwordHasher.hash("new-password")).thenReturn("hashed-password");

        useCase.execute(collaboratorId, "new-password", authUser);

        verify(collaboratorRepository, times(1)).updatePasswordHash(collaboratorId, "hashed-password");
    }

    @Test
    void shouldUpdatePasswordWhenSellerSetsOwnPassword() {
        UUID collaboratorId = UUID.randomUUID();
        Collaborator collaborator = buildCollaborator(collaboratorId);
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, collaboratorId, UUID.randomUUID());

        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(collaborator));
        when(passwordHasher.hash(any())).thenReturn("hashed-password");

        useCase.execute(collaboratorId, "my-password", authUser);

        verify(collaboratorRepository, times(1)).updatePasswordHash(any(), any());
    }

    @Test
    void shouldThrowForbiddenWhenSellerSetsOtherPassword() {
        UUID collaboratorId = UUID.randomUUID();
        Collaborator collaborator = buildCollaborator(collaboratorId);
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, UUID.randomUUID(), UUID.randomUUID());

        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(collaborator));

        assertThrows(ForbiddenException.class, () -> useCase.execute(collaboratorId, "password", authUser));

        verify(collaboratorRepository, never()).updatePasswordHash(any(), any());
        verify(passwordHasher, never()).hash(any());
    }

    @Test
    void shouldThrowCollaboratorNotFoundWhenIdDoesNotExist() {
        UUID collaboratorId = UUID.randomUUID();
        AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SELLER, UUID.randomUUID(), UUID.randomUUID());

        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.empty());

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(collaboratorId, "password", authUser));

        verify(collaboratorRepository, never()).updatePasswordHash(any(), any());
        verify(passwordHasher, never()).hash(any());
    }
}
