package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyQueryServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyQueryService companyQueryService;

    private final UUID id = UUID.randomUUID();

    @Test
    void shouldReturnEntityWhenFound() {
        Company company = mock(Company.class);
        when(companyRepository.getCompanyById(id)).thenReturn(Optional.of(company));

        Company result = companyQueryService.getOrThrowById(id);

        assertNotNull(result);
        assertSame(company, result);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(companyRepository.getCompanyById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyQueryService.getOrThrowById(id));
    }
}
