package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
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
class UpdateCompanyUseCaseTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private UpdateCompanyUseCase useCase;

    @Test
    void shouldUpdateCompanySuccessfully() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Company updated = new Company(id, "Acme PRO", "12.345.678/0001-90", null, null, true, now, now, "PRO", List.of());

        when(companyRepository.updateCompany(id, "Acme PRO", "PRO", true)).thenReturn(Optional.of(updated));

        CompanyResponseDTO result = useCase.execute(id, "Acme PRO", "PRO", true);

        assertEquals(id, result.id());
        assertEquals("Acme PRO", result.name());
        assertEquals("PRO", result.plan());
        assertTrue(result.active());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        UUID id = UUID.randomUUID();

        when(companyRepository.updateCompany(eq(id), any(), any(), anyBoolean())).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,
                () -> useCase.execute(id, "Acme PRO", "PRO", true));
    }
}
