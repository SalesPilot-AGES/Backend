package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.domain.model.AverageMeetingDurationPerMonth;
import com.salespilot.api.domain.repository.MeetingRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAverageMeetingDurationPerMonthUseCaseTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private GetAverageMeetingDurationPerMonthUseCase useCase;

    @Test
    void shouldReturnMappedDataWhenRepositoryReturnsList() {
        when(meetingRepository.groupAverageMeetingDurationPerMonth(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new AverageMeetingDurationPerMonth(LocalDateTime.now(), "Jan", 45.0),
                        new AverageMeetingDurationPerMonth(LocalDateTime.now(), "Feb", 60.0)
                ));

        GroupAverageMeetingDurationPerMonthResponseDTO result = useCase.execute("30d", null, null);

        assertEquals(2, result.data().size());
        assertEquals("Jan", result.data().get(0).monthLabel());
        assertEquals(60.0, result.data().get(1).avgMinutes());
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsNull() {
        when(meetingRepository.groupAverageMeetingDurationPerMonth(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);

        GroupAverageMeetingDurationPerMonthResponseDTO result = useCase.execute("30d", null, null);

        assertNotNull(result.data());
        assertTrue(result.data().isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnsEmptyList() {
        when(meetingRepository.groupAverageMeetingDurationPerMonth(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        GroupAverageMeetingDurationPerMonthResponseDTO result = useCase.execute("30d", null, null);

        assertTrue(result.data().isEmpty());
    }
}
