package com.salespilot.api.application.usecase;

import com.salespilot.api.application.assembler.MeetingContextMetadataAssembler;
import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.queryservice.ClientQueryService;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;

import java.util.UUID;

public class GetMeetingContextAndMetadataUseCase {
    private final MeetingQueryService meetingQueryService;
    private final CollaboratorQueryService collaboratorQueryService;
    private final ClientQueryService clientQueryService;
    private final MeetingPreAnalysisRepository meetingPreAnalysisRepository;
    private final MeetingContextMetadataAssembler assembler;

    public GetMeetingContextAndMetadataUseCase(MeetingQueryService meetingQueryService, CollaboratorQueryService collaboratorQueryService, ClientQueryService clientQueryService, MeetingPreAnalysisRepository meetingPreAnalysisRepository, MeetingContextMetadataAssembler assembler) {
        this.meetingQueryService = meetingQueryService;
        this.collaboratorQueryService = collaboratorQueryService;
        this.clientQueryService = clientQueryService;
        this.meetingPreAnalysisRepository = meetingPreAnalysisRepository;
        this.assembler = assembler;
    }

    public MeetingContextMetadataResponseDTO execute(UUID meetingId) {
        Meeting meeting = meetingQueryService.getOrThrowById(meetingId);

        Collaborator seller = collaboratorQueryService.getOrThrowById(meeting.getCollaboratorId());

        Client client = clientQueryService.getOrThrowById(meeting.getClientId());

        MeetingPreAnalysis preAnalysis = meetingPreAnalysisRepository
                .findByMeetingId(meeting.getId())
                .orElse(null);

        return assembler.toDTO(meeting, seller, client, preAnalysis);
    }
}
