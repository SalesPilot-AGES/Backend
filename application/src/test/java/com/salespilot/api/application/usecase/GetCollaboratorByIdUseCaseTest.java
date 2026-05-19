package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCollaboratorByIdUseCaseTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetCollaboratorByIdUseCase useCase;

    private final UUID companyId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("light", "gpt-4o");

    private Collaborator buildCollaborator() {
        return new Collaborator(collaboratorId, companyId, "João", "joao@acme.com", "+55 11 99999-0000", CollaboratorRole.MANAGER, true, 0, preferences, now, now);
    }

    private Collaborator buildCollaboratorWithRole(CollaboratorRole role) {
        return new Collaborator(collaboratorId, companyId, "João", "joao@acme.com", "+55 11 99999-0000", role, true, 0, preferences, now, now);
    }

    private Company buildCompany() {
        return new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    @Test
    void shouldReturnCollaboratorWithCompanyData() {
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(buildCollaborator()));
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(buildCompany()));

        CollaboratorResponseDTO result = useCase.execute(collaboratorId);

        assertEquals(collaboratorId, result.id());
        assertEquals(companyId, result.companyId());
        assertEquals("João", result.name());
        assertNotNull(result.company());
        assertEquals(companyId, result.company().id());
        assertEquals("Acme Corp", result.company().name());
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.empty());

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(collaboratorId));

        verify(companyRepository, never()).getCompanyById(any());
    }

    @Test
    void shouldThrowWhenCollaboratorRoleIsNotManager() {
        when(collaboratorRepository.getCollaboratorById(collaboratorId))
                .thenReturn(Optional.of(buildCollaboratorWithRole(CollaboratorRole.SELLER)));

        assertThrows(InvalidCollaboratorRoleException.class, () -> useCase.execute(collaboratorId));

        verify(companyRepository, never()).getCompanyById(any());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(buildCollaborator()));
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> useCase.execute(collaboratorId));
    }
}
