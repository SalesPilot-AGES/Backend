package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.CollaboratorAssembler;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.CompanyQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
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
class GetCollaboratorByIdUseCaseTest {

    @Mock
    private CollaboratorQueryService collaboratorQueryService;

    @Mock
    private CompanyQueryService companyQueryService;

    @Spy
    private CollaboratorAssembler assembler;

    @InjectMocks
    private GetCollaboratorByIdUseCase useCase;

    private final UUID companyId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("light", "gpt-4o");
    private final AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), companyId);

    private Collaborator buildCollaborator() {
        return new Collaborator(collaboratorId, companyId, "João", "joao@acme.com", "+55 11 99999-0000", CollaboratorRole.MANAGER, true, preferences, now, now);
    }

    private Collaborator buildCollaboratorWithRole(CollaboratorRole role) {
        return new Collaborator(collaboratorId, companyId, "João", "joao@acme.com", "+55 11 99999-0000", role, true, preferences, now, now);
    }

    private Company buildCompany() {
        return new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    @Test
    void shouldReturnCollaboratorWithCompanyData() {
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(companyQueryService.getOrThrowById(companyId)).thenReturn(buildCompany());

        CollaboratorResponseDTO result = useCase.execute(collaboratorId, authUser);

        assertEquals(collaboratorId, result.id());
        assertEquals(companyId, result.companyId());
        assertEquals("João", result.name());
        assertNotNull(result.company());
        assertEquals(companyId, result.company().id());
        assertEquals("Acme Corp", result.company().name());
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenThrow(new CollaboratorNotFoundException(collaboratorId));

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(collaboratorId, authUser));

        verifyNoInteractions(companyQueryService);
    }

    @Test
    void shouldThrowWhenCollaboratorRoleIsNotManager() {
        when(collaboratorQueryService.getOrThrowById(collaboratorId))
                .thenReturn(buildCollaboratorWithRole(CollaboratorRole.SELLER));

        assertThrows(InvalidCollaboratorRoleException.class, () -> useCase.execute(collaboratorId, authUser));

        verifyNoInteractions(companyQueryService);
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(companyQueryService.getOrThrowById(companyId)).thenThrow(new CompanyNotFoundException(companyId));

        assertThrows(CompanyNotFoundException.class, () -> useCase.execute(collaboratorId, authUser));
    }
}