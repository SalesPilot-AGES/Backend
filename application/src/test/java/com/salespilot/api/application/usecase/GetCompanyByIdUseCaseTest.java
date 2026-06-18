package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.CompanyResponseDTO;
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
class GetCompanyByIdUseCaseTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetCompanyByIdUseCase useCase;

    @Test
    void shouldReturnCompanyWhenExists() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Company company = new Company(id, "Acme Corp", "12.345.678/0001-90", null, null, true, now, now, "BASIC", List.of());

        when(companyRepository.getCompanyById(id)).thenReturn(Optional.of(company));

        Optional<CompanyResponseDTO> result = useCase.execute(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
        assertEquals("Acme Corp", result.get().name());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(companyRepository.getCompanyById(id)).thenReturn(Optional.empty());

        Optional<CompanyResponseDTO> result = useCase.execute(id);

        assertTrue(result.isEmpty());
    }
}
