package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllCompaniesUseCaseTest {

    @Mock
    private CompanyRepository repository;

    @InjectMocks
    private GetAllCompaniesUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final Pageable pageable = PageRequest.of(0, 10);

    private Company buildCompany(UUID id, String name) {
        return new Company(id, name, "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());
    }

    @Test
    void shouldReturnPageOfCompanies() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Page<Company> companies = new PageImpl<>(List.of(buildCompany(id1, "Acme"), buildCompany(id2, "Beta")));

        when(repository.getAllCompanies(null, null, null, null, pageable)).thenReturn(companies);

        Page<CompanyResponseDTO> result = useCase.execute(null, null, null, null, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Acme", result.getContent().get(0).name());
        assertEquals("Beta", result.getContent().get(1).name());
    }

    @Test
    void shouldReturnEmptyPageWhenNoCompaniesMatch() {
        when(repository.getAllCompanies("Nonexistent", null, null, null, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        Page<CompanyResponseDTO> result = useCase.execute("Nonexistent", null, null, null, pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldPassAllFiltersToRepository() {
        when(repository.getAllCompanies("Acme", "12.345.678/0001-90", "BASIC", true, pageable))
                .thenReturn(new PageImpl<>(List.of(buildCompany(UUID.randomUUID(), "Acme"))));

        Page<CompanyResponseDTO> result = useCase.execute("Acme", "12.345.678/0001-90", "BASIC", true, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).getAllCompanies("Acme", "12.345.678/0001-90", "BASIC", true, pageable);
    }
}
