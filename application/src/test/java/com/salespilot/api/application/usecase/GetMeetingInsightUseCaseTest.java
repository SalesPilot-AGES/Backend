package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.enums.RealtimeInsightType;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
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
    private MeetingQueryService meetingQueryService;
    @Mock
    private CollaboratorQueryService collaboratorQueryService;

    @InjectMocks
    private GetMeetingInsightUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID meetingId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

    private Meeting buildMeeting() {
        return new Meeting(meetingId, collaboratorId, UUID.randomUUID(), "Sales Call", "SCHEDULED",
                3600, "Close deal", "PRESENTIAL", null, null, null, now, now, now, now);
    }

    private Collaborator buildSeller() {
        return new Collaborator(collaboratorId, UUID.randomUUID(), "João", "joao@acme.com",
                "+55 11 99999-0000", CollaboratorRole.SELLER, true,
                new CollaboratorPreferences("light", "gpt-4o"), now, now);
    }

    private MeetingRealtimeInsight buildInsight(RealtimeInsightType type, String content) {
        return new MeetingRealtimeInsight(UUID.randomUUID(), meetingId, content, type,
                new InsightDescription("Detalhe do insight"), now);
    }

    @Test
    void shouldReturnListOfInsights() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildSeller());
        when(repository.findByMeetingId(meetingId)).thenReturn(List.of(
                buildInsight(RealtimeInsightType.KEY_POINT, "Client needs scalability"),
                buildInsight(RealtimeInsightType.ACTION_ITEM, "Send proposal by Friday")
        ));

        List<MeetingRealtimeInsightsResponseDTO> result = useCase.execute(meetingId, authUser);

        assertEquals(2, result.size());
        assertEquals(RealtimeInsightType.KEY_POINT, result.get(0).type());
        assertEquals("Client needs scalability", result.get(0).content());
        assertEquals(RealtimeInsightType.ACTION_ITEM, result.get(1).type());
    }

    @Test
    void shouldReturnEmptyListWhenNoInsights() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildSeller());
        when(repository.findByMeetingId(meetingId)).thenReturn(List.of());

        List<MeetingRealtimeInsightsResponseDTO> result = useCase.execute(meetingId, authUser);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenMeetingNotFound() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenThrow(new MeetingNotFoundException(meetingId));

        assertThrows(MeetingNotFoundException.class, () -> useCase.execute(meetingId, authUser));

        verifyNoInteractions(repository, collaboratorQueryService);
    }
}