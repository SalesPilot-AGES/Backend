package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.domain.model.CompanyStatusCount;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetGroupedCompaniesCountUseCaseTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetGroupedCompaniesCountUseCase useCase;

    @Test
    void shouldReturnActiveAndInactiveLabels() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(
                List.of(new CompanyStatusCount(true, 10L), new CompanyStatusCount(false, 3L))
        );

        GroupCompanyCountResponseDTO result = useCase.execute();

        assertEquals(2, result.data().size());
        assertEquals("Ativas", result.data().get(0).label());
        assertEquals(10L, result.data().get(0).value());
        assertEquals("Inativas", result.data().get(1).label());
        assertEquals(3L, result.data().get(1).value());
    }

    @Test
    void shouldSumTotalCorrectly() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(
                List.of(new CompanyStatusCount(true, 10L), new CompanyStatusCount(false, 3L))
        );

        GroupCompanyCountResponseDTO result = useCase.execute();

        assertEquals(13L, result.total());
    }

    @Test
    void shouldReturnZeroTotal_whenRepositoryReturnsEmpty() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(List.of());

        GroupCompanyCountResponseDTO result = useCase.execute();

        assertTrue(result.data().isEmpty());
        assertEquals(0L, result.total());
    }

    @Test
    void shouldMapInactiveCompaniesToInativasLabel() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(
                List.of(new CompanyStatusCount(false, 7L))
        );

        GroupCompanyCountResponseDTO result = useCase.execute();

        assertEquals("Inativas", result.data().get(0).label());
        assertEquals(7L, result.total());
    }
}
