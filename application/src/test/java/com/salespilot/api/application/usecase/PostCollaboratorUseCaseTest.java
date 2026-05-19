package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
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
class PostCollaboratorUseCaseTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private PostCollaboratorUseCase useCase;

    private final UUID companyId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("light", "gpt-4o");

    @Test
    void shouldCreateCollaboratorSuccessfully() {
        Company company = new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
        Collaborator collaborator = new Collaborator(collaboratorId, companyId, "João", "joao@acme.com", "+55 11 99999-0000", CollaboratorRole.SELLER, true, 0, preferences, now, now);

        when(collaboratorRepository.existsByCompanyIdAndEmail(companyId, "joao@acme.com")).thenReturn(false);
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(company));
        when(collaboratorRepository.create(companyId, "João", "joao@acme.com", CollaboratorRole.SELLER, true, "+55 11 99999-0000", preferences, 0)).thenReturn(collaborator);

        CollaboratorResponseDTO result = useCase.create(companyId, "João", "joao@acme.com", CollaboratorRole.SELLER, true, "+55 11 99999-0000", preferences, 0);

        assertEquals(collaboratorId, result.id());
        assertEquals(companyId, result.companyId());
        assertEquals("João", result.name());
        assertEquals(CollaboratorRole.SELLER, result.role());
        assertEquals("joao@acme.com", result.email());
        assertNotNull(result.company());
        assertEquals(companyId, result.company().id());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExistsForCompany() {
        when(collaboratorRepository.existsByCompanyIdAndEmail(companyId, "joao@acme.com")).thenReturn(true);

        assertThrows(CollaboratorAlreadyExistsException.class,
                () -> useCase.create(companyId, "João", "joao@acme.com", CollaboratorRole.SELLER, true, "+55 11 99999-0000", preferences, 0));

        verify(collaboratorRepository, never()).create(any(), any(), any(), any(), anyBoolean(), any(), any(), any());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(collaboratorRepository.existsByCompanyIdAndEmail(companyId, "joao@acme.com")).thenReturn(false);
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,
                () -> useCase.create(companyId, "João", "joao@acme.com", CollaboratorRole.SELLER, true, "+55 11 99999-0000", preferences, 0));

        verify(collaboratorRepository, never()).create(any(), any(), any(), any(), anyBoolean(), any(), any(), any());
    }
}
