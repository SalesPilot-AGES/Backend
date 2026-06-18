package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.MeetingAssembler;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingPageResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllMeetingsUseCaseTest {

    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private MeetingPostAnalysisRepository meetingPostAnalysisRepository;
    @Mock
    private CollaboratorQueryService collaboratorQueryService;
    @Mock
    private ClientQueryService clientQueryService;
    @Spy
    private MeetingAssembler assembler;

    @InjectMocks
    private GetAllMeetingsUseCase useCase;

    private final LocalDateTime now = LocalDateTime.now();
    private final Pageable pageable = PageRequest.of(0, 10);
    private final UUID meetingId = UUID.randomUUID();
    private final UUID collaboratorId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();
    private final AuthUserDTO authUser = new AuthUserDTO(CollaboratorRole.SYSTEM_ADMIN, UUID.randomUUID(), UUID.randomUUID());

    private Meeting buildMeeting() {
        return new Meeting(meetingId, collaboratorId, clientId, "Sales Call", "SCHEDULED",
                3600, "Close deal", "PRESENTIAL", null, null, null, now, now, now, now);
    }

    private Collaborator buildCollaborator() {
        return new Collaborator(collaboratorId, UUID.randomUUID(), "João", "joao@acme.com",
                "+55 11 99999-0000", CollaboratorRole.SELLER, true,
                new CollaboratorPreferences("light", "gpt-4o"), now, now);
    }

    private Client buildClient() {
        return new Client(clientId, UUID.randomUUID(), collaboratorId, "Empresa X",
                "Tech Corp", "Technology", 8, now, now);
    }

    @Test
    void shouldReturnMeetingPageWithSummary() {
        Page<Meeting> meetingPage = new PageImpl<>(List.of(buildMeeting()), PageRequest.of(0, 10), 1);

        when(meetingRepository.getAllMeetings(null, null, null, null, pageable)).thenReturn(meetingPage);
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(clientQueryService.getOrThrowById(clientId)).thenReturn(buildClient());
        when(meetingRepository.getTotalMeetings()).thenReturn(1L);
        when(meetingRepository.getAverageDurationSeconds()).thenReturn(3600.0);
        when(meetingPostAnalysisRepository.getAverageSuccessRate()).thenReturn(75.0);

        MeetingPageResponseDTO result = useCase.execute(null, null, null, pageable, authUser);

        assertEquals(1, result.totalElements());
        assertEquals(1, result.content().size());
        assertEquals("Sales Call", result.content().get(0).title());
        assertEquals(1L, result.summary().totalMeetings());
        assertEquals(3600.0, result.summary().averageDurationSeconds());
        assertEquals(75.0, result.summary().successRate());
    }

    @Test
    void shouldReturnEmptyPageWithSummaryWhenNoMeetings() {
        when(meetingRepository.getAllMeetings(null, null, null, null, pageable))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));
        when(meetingRepository.getTotalMeetings()).thenReturn(0L);
        when(meetingRepository.getAverageDurationSeconds()).thenReturn(0.0);
        when(meetingPostAnalysisRepository.getAverageSuccessRate()).thenReturn(null);

        MeetingPageResponseDTO result = useCase.execute(null, null, null, pageable, authUser);

        assertTrue(result.content().isEmpty());
        assertEquals(0L, result.summary().totalMeetings());
    }

    @Test
    void shouldThrowWhenCollaboratorNotFound() {
        when(meetingRepository.getAllMeetings(null, null, null, null, pageable))
                .thenReturn(new PageImpl<>(List.of(buildMeeting()), PageRequest.of(0, 10), 1));
        when(collaboratorQueryService.getOrThrowById(collaboratorId))
                .thenThrow(new CollaboratorNotFoundException(collaboratorId));

        assertThrows(CollaboratorNotFoundException.class, () -> useCase.execute(null, null, null, pageable, authUser));
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(meetingRepository.getAllMeetings(null, null, null, null, pageable))
                .thenReturn(new PageImpl<>(List.of(buildMeeting()), PageRequest.of(0, 10), 1));
        when(collaboratorQueryService.getOrThrowById(collaboratorId)).thenReturn(buildCollaborator());
        when(clientQueryService.getOrThrowById(clientId)).thenThrow(new ClientNotFoundException(clientId));

        assertThrows(ClientNotFoundException.class, () -> useCase.execute(null, null, null, pageable, authUser));
    }
}