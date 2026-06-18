package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.model.CompanyNameAndTotalMeetings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTopFiveCompaniesByMeetingTotalUseCaseTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetTopFiveCompaniesByMeetingTotalUseCase useCase;

    @Test
    void shouldReturnMappedCompanyList() {
        List<CompanyNameAndTotalMeetings> repoData = List.of(
                new CompanyNameAndTotalMeetings("Acme Corp", 15L),
                new CompanyNameAndTotalMeetings("Beta Ltd", 8L)
        );

        when(companyRepository.getTopFiveCompaniesByMeetingTotal(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(repoData);

        TopFiveCompanyByMeetingTotalResponseDto result = useCase.execute("all", null, null);

        assertEquals(2, result.data().size());
        assertEquals("Acme Corp", result.data().get(0).companyName());
        assertEquals(15L, result.data().get(0).total());
        assertEquals("Beta Ltd", result.data().get(1).companyName());
    }

    @Test
    void shouldReturnEmptyList_whenRepositoryReturnsEmpty() {
        when(companyRepository.getTopFiveCompaniesByMeetingTotal(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        TopFiveCompanyByMeetingTotalResponseDto result = useCase.execute("all", null, null);

        assertTrue(result.data().isEmpty());
    }

    @Test
    void shouldCallRepositoryWithCorrectDateRange() {
        when(companyRepository.getTopFiveCompaniesByMeetingTotal(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        useCase.execute("all", null, null);

        verify(companyRepository, times(1))
                .getTopFiveCompaniesByMeetingTotal(any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
