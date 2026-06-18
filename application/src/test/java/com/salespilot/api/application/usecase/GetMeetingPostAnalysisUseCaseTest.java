package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingPostAnalysisResponseDTO;
import com.salespilot.api.application.exception.MeetingPostAnalysisNotFoundException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
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
    @Mock
    private MeetingQueryService meetingQueryService;
    @Mock
    private CollaboratorQueryService collaboratorQueryService;

    @InjectMocks
    private GetMeetingPostAnalysisUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID meetingId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

    private Meeting buildMeeting() {
        return new Meeting(meetingId, collaboratorId, UUID.randomUUID(), "Sales Call", "DONE",
                3600, null, "PRESENTIAL", null, null, null, now, now, now, now);
    }

    private Collaborator buildSeller() {
        return new Collaborator(collaboratorId, UUID.randomUUID(), "João", "joao@acme.com",
                "+55 11 99999-0000", CollaboratorRole.SELLER, true,
                new CollaboratorPreferences("light", "gpt-4o"), now, now);
    }

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
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildSeller());
        when(meetingPostAnalysisRepository.findByMeetingId(meetingId))
                .thenReturn(Optional.of(buildPostAnalysis()));

        MeetingPostAnalysisResponseDTO result = useCase.execute(meetingId, authUser);

        assertEquals(meetingId, result.meetingId());
        assertEquals("The meeting went well overall.", result.summary());
        assertEquals(1, result.actionItems().size());
        assertEquals("Send proposal", result.actionItems().get(0).text());
        assertEquals("positive", result.sentimentAnalysis().overall());
        assertEquals(0.85, result.sentimentAnalysis().score());
    }

    @Test
    void shouldThrowWhenPostAnalysisNotFound() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildSeller());
        when(meetingPostAnalysisRepository.findByMeetingId(meetingId)).thenReturn(Optional.empty());

        assertThrows(MeetingPostAnalysisNotFoundException.class, () -> useCase.execute(meetingId, authUser));
    }
}
