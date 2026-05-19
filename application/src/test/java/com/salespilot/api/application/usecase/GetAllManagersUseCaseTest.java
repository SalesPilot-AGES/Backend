package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllManagersUseCaseTest {

    @Mock
    private CollaboratorRepository repository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetAllManagersUseCase useCase;

    private final UUID companyId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final CollaboratorPreferences preferences = new CollaboratorPreferences("light", "gpt-4o");
    private final PageRequest pageable = PageRequest.of(0, 10);

    private Collaborator buildManager() {
        return new Collaborator(collaboratorId, companyId, "Ana", "ana@acme.com", "+55 11 99999-0000", CollaboratorRole.MANAGER, true, 0, preferences, now, now);
    }

    private Company buildCompany() {
        return new Company(companyId, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    @Test
    void shouldReturnPageOfManagers() {
        Page<Collaborator> managersPage = new PageImpl<>(List.of(buildManager()), pageable, 1);

        when(repository.getManagers(null, null, companyId, true, pageable)).thenReturn(managersPage);
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.of(buildCompany()));

        Page<CollaboratorResponseDTO> result = useCase.execute(null, null, companyId, true, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(collaboratorId, result.getContent().get(0).id());
        assertEquals("Ana", result.getContent().get(0).name());
        assertNotNull(result.getContent().get(0).company());
    }

    @Test
    void shouldThrowWhenCompanyNotFoundForManager() {
        Page<Collaborator> managersPage = new PageImpl<>(List.of(buildManager()), pageable, 1);

        when(repository.getManagers(null, null, companyId, null, pageable)).thenReturn(managersPage);
        when(companyRepository.getCompanyById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,
                () -> useCase.execute(null, null, companyId, null, pageable));
    }
}
