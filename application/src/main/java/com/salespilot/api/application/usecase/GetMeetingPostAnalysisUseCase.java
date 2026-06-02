package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.MeetingPostAnalysisResponseDTO;
import com.salespilot.api.application.exception.MeetingPostAnalysisNotFoundException;
import com.salespilot.api.application.queryservice.CollaboratorQueryService;
import com.salespilot.api.application.queryservice.MeetingQueryService;
import com.salespilot.api.application.utils.MeetingAccessUtils;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;

import java.util.UUID;

public class GetMeetingPostAnalysisUseCase {
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;
    private final MeetingQueryService meetingQueryService;
    private final CollaboratorQueryService collaboratorQueryService;

    public GetMeetingPostAnalysisUseCase(MeetingPostAnalysisRepository meetingPostAnalysisRepository, MeetingQueryService meetingQueryService, CollaboratorQueryService collaboratorQueryService) {
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
        this.meetingQueryService = meetingQueryService;
        this.collaboratorQueryService = collaboratorQueryService;
    }

    public MeetingPostAnalysisResponseDTO execute(UUID meetingId, AuthUserDTO authUser) {
        Meeting meeting = meetingQueryService.getOrThrowById(meetingId);

        Collaborator seller = collaboratorQueryService.getOrThrowById(meeting.getCollaboratorId());

        MeetingAccessUtils.grantAccess(seller.getId(), seller.getCompanyId(), authUser);

        return meetingPostAnalysisRepository
                .findByMeetingId(meetingId)
                .map(MeetingPostAnalysisResponseDTO::from)
                .orElseThrow(() -> new MeetingPostAnalysisNotFoundException(meetingId));
    }
}
