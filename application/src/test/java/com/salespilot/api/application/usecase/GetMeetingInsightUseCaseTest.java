package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.enums.RealtimeInsightType;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.valueobject.InsightDescription;
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
class GetMeetingInsightUseCaseTest {

    @Mock
    private MeetingRealtimeInsightRepository repository;
    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private GetMeetingInsightUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID meetingId = UUID.randomUUID();

    private MeetingRealtimeInsight buildInsight(RealtimeInsightType type, String content) {
        return new MeetingRealtimeInsight(UUID.randomUUID(), meetingId, content, type,
                new InsightDescription("Detalhe do insight"), now);
    }

    @Test
    void shouldReturnListOfInsights() {
        when(meetingRepository.existsById(meetingId)).thenReturn(true);
        when(repository.findByMeetingId(meetingId)).thenReturn(List.of(
                buildInsight(RealtimeInsightType.KEY_POINT, "Client needs scalability"),
                buildInsight(RealtimeInsightType.ACTION_ITEM, "Send proposal by Friday")
        ));

        List<MeetingRealtimeInsightsResponseDTO> result = useCase.execute(meetingId);

        assertEquals(2, result.size());
        assertEquals(RealtimeInsightType.KEY_POINT, result.get(0).type());
        assertEquals("Client needs scalability", result.get(0).content());
        assertEquals(RealtimeInsightType.ACTION_ITEM, result.get(1).type());
    }

    @Test
    void shouldReturnEmptyListWhenNoInsights() {
        when(meetingRepository.existsById(meetingId)).thenReturn(true);
        when(repository.findByMeetingId(meetingId)).thenReturn(List.of());

        List<MeetingRealtimeInsightsResponseDTO> result = useCase.execute(meetingId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenMeetingNotFound() {
        when(meetingRepository.existsById(meetingId)).thenReturn(false);

        assertThrows(MeetingNotFoundException.class, () -> useCase.execute(meetingId));

        verifyNoInteractions(repository);
    }
}
