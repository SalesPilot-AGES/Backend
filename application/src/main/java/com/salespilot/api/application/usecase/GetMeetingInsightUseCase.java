package com.salespilot.api.application.usecase;

import java.util.UUID;

import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;

public class GetMeetingInsightUseCase {
    private MeetingRealtimeInsightRepository meetingRealtimeInsightRepository;

    public GetMeetingInsightUseCase( MeetingRealtimeInsightRepository meetingRealtimeInsightRepository){
        this.meetingRealtimeInsightRepository = meetingRealtimeInsightRepository;
    }

    public MeetingRealtimeInsightsResponseDTO execute(UUID meetingId){
        
            
    }
}
