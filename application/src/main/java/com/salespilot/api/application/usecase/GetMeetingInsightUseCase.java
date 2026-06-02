package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.application.utils.MeetingAccessUtils;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;

import java.util.List;
import java.util.UUID;

public class GetMeetingInsightUseCase {
    private final MeetingRealtimeInsightRepository repository;
    private final MeetingQueryService meetingQueryService;
    private final CollaboratorQueryService collaboratorQueryService;

    public GetMeetingInsightUseCase(MeetingRealtimeInsightRepository repository, MeetingQueryService meetingQueryService, CollaboratorQueryService collaboratorQueryService){
        this.repository = repository;
        this.meetingQueryService = meetingQueryService;
        this.collaboratorQueryService = collaboratorQueryService;
    }

    public List<MeetingRealtimeInsightsResponseDTO> execute(UUID meetingId, AuthUserDTO authUser){
        Meeting meeting = meetingQueryService.getOrThrowById(meetingId);

        Collaborator seller = collaboratorQueryService.getOrThrowById(meeting.getCollaboratorId());

        MeetingAccessUtils.grantAccess(seller.getId(), seller.getCompanyId(), authUser);

        return repository
            .findByMeetingId(meetingId)
            .stream()
            .map(MeetingRealtimeInsightsResponseDTO::from)
            .toList();
    }
}
