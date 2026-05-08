package com.salespilot.api.application.usecase;

import java.util.List;
import java.util.UUID;

import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.domain.repository.MeetingRepository;

public class GetMeetingInsightUseCase {
    private final MeetingRealtimeInsightRepository repository;
    private final MeetingRepository meetingRepository;

    public GetMeetingInsightUseCase( MeetingRealtimeInsightRepository repository, MeetingRepository meetingRepository){
        this.repository = repository;
        this.meetingRepository = meetingRepository;
    }

    public List<MeetingRealtimeInsightsResponseDTO> execute(UUID meetingId){
        if (!meetingRepository.existsById(meetingId)) {
            throw new MeetingNotFoundException(meetingId);
        }

        return repository
            .findByMeetingId(meetingId)
            .stream()
            .map(MeetingRealtimeInsightsResponseDTO::from)
            .toList();
    }
}
