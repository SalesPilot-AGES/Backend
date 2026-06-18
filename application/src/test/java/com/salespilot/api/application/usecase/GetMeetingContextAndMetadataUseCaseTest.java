package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.MeetingContextMetadataAssembler;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMeetingContextAndMetadataUseCaseTest {

    @Mock
    private MeetingQueryService meetingQueryService;
    @Mock
    private CollaboratorQueryService collaboratorQueryService;
    @Mock
    private ClientQueryService clientQueryService;
    @Mock
    private MeetingPreAnalysisRepository meetingPreAnalysisRepository;
    @Spy
    private MeetingContextMetadataAssembler assembler;

    @InjectMocks
    private GetMeetingContextAndMetadataUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID meetingId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();
    private final AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

    private Meeting buildMeeting() {
        return new Meeting(meetingId, collaboratorId, clientId, "Discovery Call", "SCHEDULED",
                1800, "Understand needs", "REMOTE", "Reduce costs", "First contact",
                "None", now, now, now, now);
    }

    private Collaborator buildCollaborator() {
        return new Collaborator(collaboratorId, UUID.randomUUID(), "Carlos", "carlos@acme.com",
                null, CollaboratorRole.SELLER, true,
                new CollaboratorPreferences("dark", "gpt-4o"), now, now);
    }

    private Client buildClient() {
        return new Client(clientId, UUID.randomUUID(), collaboratorId, "Maria",
                "Tech Corp", "Technology", 7, now, now);
    }

    private MeetingPreAnalysis buildPreAnalysis() {
        return new MeetingPreAnalysis(UUID.randomUUID(), meetingId,
                new PreAnalysisRecommendedStrategy("Build rapport"),
                List.of("Point A", "Point B"),
                List.of("Objection 1"),
                now);
    }

    @Test
    void shouldReturnMeetingContextWithPreAnalysis() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(clientQueryService.getOrThrowById(clientId)).thenReturn(buildClient());
        when(meetingPreAnalysisRepository.findByMeetingId(meetingId)).thenReturn(Optional.of(buildPreAnalysis()));

        MeetingContextMetadataResponseDTO result = useCase.execute(meetingId, authUser);

        assertEquals(meetingId, result.id());
        assertEquals("Discovery Call", result.title());
        assertEquals("Carlos", result.seller().name());
        assertEquals("Maria", result.client().name());
        assertNotNull(result.preAnalysis());
        assertEquals("Build rapport", result.preAnalysis().recommendedStrategy().focus());
    }

    @Test
    void shouldReturnMeetingContextWithNullPreAnalysisWhenNotFound() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(clientQueryService.getOrThrowById(clientId)).thenReturn(buildClient());
        when(meetingPreAnalysisRepository.findByMeetingId(meetingId)).thenReturn(Optional.empty());

        MeetingContextMetadataResponseDTO result = useCase.execute(meetingId, authUser);

        assertEquals(meetingId, result.id());
        assertNull(result.preAnalysis());
    }

    @Test
    void shouldThrowWhenMeetingNotFound() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenThrow(new MeetingNotFoundException(meetingId));

        assertThrows(MeetingNotFoundException.class, () -> useCase.execute(meetingId, authUser));

        verifyNoInteractions(collaboratorQueryService, clientQueryService, meetingPreAnalysisRepository);
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenThrow(new CollaboratorNotFoundException(collaboratorId));

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(meetingId, authUser));

        verifyNoInteractions(clientQueryService, meetingPreAnalysisRepository);
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(meetingQueryService.getOrThrowById(meetingId)).thenReturn(buildMeeting());
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(clientQueryService.getOrThrowById(clientId)).thenThrow(new ClientNotFoundException(clientId));

        assertThrows(ClientNotFoundException.class, () -> useCase.execute(meetingId, authUser));

        verifyNoInteractions(meetingPreAnalysisRepository);
    }
}