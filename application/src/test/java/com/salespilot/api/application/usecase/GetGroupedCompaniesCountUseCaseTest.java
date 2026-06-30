package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.GroupStatusCountResponseDTO;
import com.salespilot.api.domain.model.StatusCount;
import com.salespilot.api.domain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetGroupedCompaniesCountUseCaseTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetGroupedCompaniesCountUseCase useCase;

    @Test
    void shouldReturnActiveAndInactiveLabels() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(
                List.of(new StatusCount(true, 10L), new StatusCount(false, 3L))
        );

        GroupStatusCountResponseDTO result = useCase.execute();

        assertEquals(2, result.data().size());
        assertEquals("Ativas", result.data().get(0).label());
        assertEquals(10L, result.data().get(0).value());
        assertEquals("Inativas", result.data().get(1).label());
        assertEquals(3L, result.data().get(1).value());
    }

    @Test
    void shouldSumTotalCorrectly() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(
                List.of(new StatusCount(true, 10L), new StatusCount(false, 3L))
        );

        GroupStatusCountResponseDTO result = useCase.execute();

        assertEquals(13L, result.total());
    }

    @Test
    void shouldReturnZeroTotal_whenRepositoryReturnsEmpty() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(List.of());

        GroupStatusCountResponseDTO result = useCase.execute();

        assertTrue(result.data().isEmpty());
        assertEquals(0L, result.total());
    }

    @Test
    void shouldMapInactiveCompaniesToInativasLabel() {
        when(companyRepository.countCompaniesGroupedByStatus()).thenReturn(
                List.of(new StatusCount(false, 7L))
        );

        GroupStatusCountResponseDTO result = useCase.execute();

        assertEquals("Inativas", result.data().get(0).label());
        assertEquals(7L, result.total());
    }
}
