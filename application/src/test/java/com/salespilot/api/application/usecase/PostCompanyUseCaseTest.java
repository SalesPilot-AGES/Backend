package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.TaxIdAlreadyExists;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostCompanyUseCaseTest {

    @Mock
    private CompanyRepository repository;

    @InjectMocks
    private PostCompanyUseCase useCase;

    @Test
    void shouldCreateCompanySuccessfully() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Company company = new Company(id, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());

        when(repository.existsByTaxId("12.345.678/0001-90")).thenReturn(false);
        when(repository.createCompany("Acme Corp", "12.345.678/0001-90", "BASIC", true)).thenReturn(company);

        CompanyResponseDTO result = useCase.create("Acme Corp", "12.345.678/0001-90", "BASIC", true);

        assertEquals(id, result.id());
        assertEquals("Acme Corp", result.name());
        assertEquals("12.345.678/0001-90", result.taxId());
        assertEquals("BASIC", result.plan());
        assertTrue(result.active());
        verify(repository).createCompany("Acme Corp", "12.345.678/0001-90", "BASIC", true);
    }

    @Test
    void shouldThrowWhenTaxIdAlreadyExists() {
        when(repository.existsByTaxId("12.345.678/0001-90")).thenReturn(true);

        assertThrows(TaxIdAlreadyExists.class,
                () -> useCase.create("Acme Corp", "12.345.678/0001-90", "BASIC", true));

        verify(repository, never()).createCompany(any(), any(), any(), anyBoolean());
    }
}
