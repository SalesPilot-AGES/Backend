package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.MeetingPostAnalysisResponseDTO;
import com.salespilot.api.application.exception.MeetingPostAnalysisNotFoundException;
import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.valueobject.PostAnalysisActionItem;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;
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
class GetMeetingPostAnalysisUseCaseTest {

    @Mock
    private MeetingPostAnalysisRepository meetingPostAnalysisRepository;

    @InjectMocks
    private GetMeetingPostAnalysisUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID meetingId = UUID.randomUUID();

    private MeetingPostAnalysis buildPostAnalysis() {
        return new MeetingPostAnalysis(
                UUID.randomUUID(),
                meetingId,
                "The meeting went well overall.",
                List.of(new PostAnalysisActionItem("Send proposal", false)),
                new PostAnalysisSentimentAnalysis("positive", 0.85),
                now
        );
    }

    @Test
    void shouldReturnPostAnalysisWhenFound() {
        when(meetingPostAnalysisRepository.findByMeetingId(meetingId))
                .thenReturn(Optional.of(buildPostAnalysis()));

        MeetingPostAnalysisResponseDTO result = useCase.execute(meetingId);

        assertEquals(meetingId, result.meetingId());
        assertEquals("The meeting went well overall.", result.summary());
        assertEquals(1, result.actionItems().size());
        assertEquals("Send proposal", result.actionItems().get(0).text());
        assertEquals("positive", result.sentimentAnalysis().overall());
        assertEquals(0.85, result.sentimentAnalysis().score());
    }

    @Test
    void shouldThrowWhenPostAnalysisNotFound() {
        when(meetingPostAnalysisRepository.findByMeetingId(meetingId)).thenReturn(Optional.empty());

        assertThrows(MeetingPostAnalysisNotFoundException.class, () -> useCase.execute(meetingId));
    }
}
