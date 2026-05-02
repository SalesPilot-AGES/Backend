package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.MeetingPostAnalysisResponseDTO;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;

import java.util.UUID;

public class GetMeetingPostAnalysisUseCase {
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;

    public GetMeetingPostAnalysisUseCase(MeetingPostAnalysisRepository meetingPostAnalysisRepository) {
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
    }

    public MeetingPostAnalysisResponseDTO execute(UUID meetingId) {
        return meetingPostAnalysisRepository
                .findByMeetingId(meetingId)
                .map(MeetingPostAnalysisResponseDTO::from)
                .orElse(null);
    }
}
