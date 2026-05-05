package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.ClientMeetingResponseDTO;
import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.dto.MeetingPreAnalysisResponseDTO;
import com.salespilot.api.application.dto.SellerMeetingResponseDTO;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.ClientRepository;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;

import java.util.UUID;

public class GetMeetingContextAndMetadataUseCase {
    private final MeetingRepository repository;
    private final CollaboratorRepository collaboratorRepository;
    private final ClientRepository clientRepository;
    private final MeetingPreAnalysisRepository meetingPreAnalysisRepository;

    public GetMeetingContextAndMetadataUseCase(MeetingRepository repository, CollaboratorRepository collaboratorRepository, ClientRepository clientRepository, MeetingPreAnalysisRepository meetingPreAnalysisRepository) {
        this.repository = repository;
        this.collaboratorRepository = collaboratorRepository;
        this.clientRepository = clientRepository;
        this.meetingPreAnalysisRepository = meetingPreAnalysisRepository;
    }

    public MeetingContextMetadataResponseDTO execute(UUID meetingId) {
        Meeting meeting = repository.getMeetingById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(meetingId));

        SellerMeetingResponseDTO seller = collaboratorRepository
                .getCollaboratorById(meeting.getCollaboratorId())
                .map(SellerMeetingResponseDTO::from)
                .orElseThrow(() -> new CollaboratorNotFoundException(meeting.getCollaboratorId()));

        ClientMeetingResponseDTO client = clientRepository
                .findById(meeting.getClientId())
                .map(ClientMeetingResponseDTO::from)
                .orElseThrow(() -> new ClientNotFoundException(meeting.getClientId()));

        MeetingPreAnalysisResponseDTO preAnalysis = meetingPreAnalysisRepository
                .findByMeetingId(meeting.getId())
                .map(MeetingPreAnalysisResponseDTO::from)
                .orElse(null);

        return new MeetingContextMetadataResponseDTO(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getStatus(),
                meeting.getMeetingType(),
                meeting.getObjective(),
                meeting.getClientNeeds(),
                meeting.getPreviousInteractions(),
                meeting.getCompetitorsInvolved(),
                meeting.getScheduledFor(),
                meeting.getStartedAt(),
                meeting.getEndedAt(),
                meeting.getDurationSeconds(),
                seller,
                client,
                preAnalysis,
                meeting.getCreatedAt()
        );
    }
}
