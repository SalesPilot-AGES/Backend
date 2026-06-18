package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditCollaboratorUseCaseTest {

    @Mock
    private CollaboratorRepository repository;

    @Mock
    private CollaboratorQueryService collaboratorQueryService;

    @Mock
    private CompanyQueryService companyQueryService;

    @Spy
    private CollaboratorAssembler assembler;

    @InjectMocks
    private EditCollaboratorUseCase useCase;

    private final UUID companyId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("dark", "gpt-4o");
    private final AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

    private Company buildCompany() {
        return new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    private Collaborator buildExisting(UUID cId, String email, CollaboratorRole role) {
        return new Collaborator(collaboratorId, cId, "João", email, "+55 11 99999-0000", role, true, preferences, now, now);
    }

    @Test
    void shouldEditCollaboratorSuccessfullyWithNoChanges() {
        Collaborator existing = buildExisting(companyId, "joao@acme.com", CollaboratorRole.SELLER);
        Collaborator updated = buildExisting(companyId, "joao@acme.com", CollaboratorRole.SELLER);

        when(companyQueryService.getOrThrowById(companyId)).thenReturn(buildCompany());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(existing);
        when(repository.update(companyId, collaboratorId, "João", "joao@acme.com", "+55 11 99999-0000", true, preferences)).thenReturn(updated);

        CollaboratorResponseDTO result = useCase.execute(companyId, collaboratorId, "João", "joao@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.SELLER, authUser);

        assertEquals(collaboratorId, result.id());
        verify(repository, never()).existsByCompanyIdAndEmail(any(), any());
    }

    @Test
    void shouldEditCollaboratorSuccessfullyWhenEmailChangedAndNotDuplicated() {
        Collaborator existing = buildExisting(companyId, "joao@acme.com", CollaboratorRole.SELLER);
        Collaborator updated = buildExisting(companyId, "novo@acme.com", CollaboratorRole.SELLER);

        when(companyQueryService.getOrThrowById(companyId)).thenReturn(buildCompany());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(existing);
        when(repository.existsByCompanyIdAndEmail(companyId, "novo@acme.com")).thenReturn(false);
        when(repository.update(companyId, collaboratorId, "João", "novo@acme.com", "+55 11 99999-0000", true, preferences)).thenReturn(updated);

        CollaboratorResponseDTO result = useCase.execute(companyId, collaboratorId, "João", "novo@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.SELLER, authUser);

        assertEquals(collaboratorId, result.id());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(companyQueryService.getOrThrowById(companyId)).thenThrow(new CompanyNotFoundException(companyId));

        assertThrows(CompanyNotFoundException.class,
                () -> useCase.execute(companyId, collaboratorId, "João", "joao@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.SELLER, authUser));

        verify(repository, never()).update(any(), any(), any(), any(), any(), anyBoolean(), any());
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(companyQueryService.getOrThrowById(companyId)).thenReturn(buildCompany());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenThrow(new CollaboratorNotFoundException(collaboratorId));

        assertThrows(CollaboratorNotFoundException.class,
                () -> useCase.execute(companyId, collaboratorId, "João", "joao@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.SELLER, authUser));

        verify(repository, never()).update(any(), any(), any(), any(), any(), anyBoolean(), any());
    }

    @Test
    void shouldThrowWhenRoleDoesNotMatch() {
        Collaborator existing = buildExisting(companyId, "joao@acme.com", CollaboratorRole.SELLER);

        when(companyQueryService.getOrThrowById(companyId)).thenReturn(buildCompany());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(existing);

        assertThrows(InvalidCollaboratorRoleException.class,
                () -> useCase.execute(companyId, collaboratorId, "João", "joao@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.MANAGER, authUser));

        verify(repository, never()).update(any(), any(), any(), any(), any(), anyBoolean(), any());
    }

    @Test
    void shouldThrowWhenEmailChangedToAnExistingOne() {
        Collaborator existing = buildExisting(companyId, "joao@acme.com", CollaboratorRole.SELLER);

        when(companyQueryService.getOrThrowById(companyId)).thenReturn(buildCompany());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(existing);
        when(repository.existsByCompanyIdAndEmail(companyId, "outro@acme.com")).thenReturn(true);

        assertThrows(CollaboratorAlreadyExistsException.class,
                () -> useCase.execute(companyId, collaboratorId, "João", "outro@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.SELLER, authUser));

        verify(repository, never()).update(any(), any(), any(), any(), any(), anyBoolean(), any());
    }

    @Test
    void shouldThrowWhenCompanyChangedAndEmailAlreadyExistsInNewCompany() {
        UUID newCompanyId = UUID.randomUUID();
        Collaborator existing = buildExisting(companyId, "joao@acme.com", CollaboratorRole.SELLER);
        Company newCompany = new Company(newCompanyId, "Nova Corp", "98.765.432/0001-10", null, null, true, now, now, "PRO", List.of());

        when(companyQueryService.getOrThrowById(newCompanyId)).thenReturn(newCompany);
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(existing);
        when(repository.existsByCompanyIdAndEmail(newCompanyId, "joao@acme.com")).thenReturn(true);

        assertThrows(CollaboratorAlreadyExistsException.class,
                () -> useCase.execute(newCompanyId, collaboratorId, "João", "joao@acme.com", "+55 11 99999-0000", true, preferences, CollaboratorRole.SELLER, authUser));

        verify(repository, never()).update(any(), any(), any(), any(), any(), anyBoolean(), any());
    }
}
