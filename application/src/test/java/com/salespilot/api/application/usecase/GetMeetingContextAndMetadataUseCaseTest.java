package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;
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
class GetMeetingContextAndMetadataUseCaseTest {

    @Mock
    private MeetingRepository repository;
    @Mock
    private CollaboratorRepository collaboratorRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private MeetingPreAnalysisRepository meetingPreAnalysisRepository;

    @InjectMocks
    private GetMeetingContextAndMetadataUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final UUID meetingId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();

    private Meeting buildMeeting() {
        return new Meeting(meetingId, collaboratorId, clientId, "Discovery Call", "SCHEDULED",
                1800, "Understand needs", "REMOTE", "Reduce costs", "First contact",
                "None", now, now, now, now);
    }

    private Collaborator buildCollaborator() {
        return new Collaborator(collaboratorId, UUID.randomUUID(), "Carlos", "carlos@acme.com",
                null, CollaboratorRole.SELLER, true, 0,
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
        when(repository.getMeetingById(meetingId)).thenReturn(Optional.of(buildMeeting()));
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(buildCollaborator()));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(buildClient()));
        when(meetingPreAnalysisRepository.findByMeetingId(meetingId)).thenReturn(Optional.of(buildPreAnalysis()));

        MeetingContextMetadataResponseDTO result = useCase.execute(meetingId);

        assertEquals(meetingId, result.id());
        assertEquals("Discovery Call", result.title());
        assertEquals("Carlos", result.seller().name());
        assertEquals("Maria", result.client().name());
        assertNotNull(result.preAnalysis());
        assertEquals("Build rapport", result.preAnalysis().recommendedStrategy().focus());
    }

    @Test
    void shouldReturnMeetingContextWithNullPreAnalysisWhenNotFound() {
        when(repository.getMeetingById(meetingId)).thenReturn(Optional.of(buildMeeting()));
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(buildCollaborator()));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(buildClient()));
        when(meetingPreAnalysisRepository.findByMeetingId(meetingId)).thenReturn(Optional.empty());

        MeetingContextMetadataResponseDTO result = useCase.execute(meetingId);

        assertEquals(meetingId, result.id());
        assertNull(result.preAnalysis());
    }

    @Test
    void shouldThrowWhenMeetingNotFound() {
        when(repository.getMeetingById(meetingId)).thenReturn(Optional.empty());

        assertThrows(MeetingNotFoundException.class, () -> useCase.execute(meetingId));

        verifyNoInteractions(collaboratorRepository, clientRepository, meetingPreAnalysisRepository);
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(repository.getMeetingById(meetingId)).thenReturn(Optional.of(buildMeeting()));
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.empty());

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(meetingId));

        verifyNoInteractions(clientRepository, meetingPreAnalysisRepository);
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(repository.getMeetingById(meetingId)).thenReturn(Optional.of(buildMeeting()));
        when(collaboratorRepository.getCollaboratorById(collaboratorId)).thenReturn(Optional.of(buildCollaborator()));
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> useCase.execute(meetingId));

        verifyNoInteractions(meetingPreAnalysisRepository);
    }
}
