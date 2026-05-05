package com.salespilot.api.application.usecase;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;

public class GetMeetingInsightUseCase {
    private MeetingRealtimeInsightRepository repository;

    public GetMeetingInsightUseCase( MeetingRealtimeInsightRepository repository){
        this.repository = repository;
    }

    public Page<MeetingRealtimeInsightsResponseDTO> execute(UUID meetingId, Pageable pageable){
        Page<MeetingRealtimeInsight> insights = repository.findByMeetingId(meetingId, pageable);

        return insights.map(MeetingRealtimeInsightsResponseDTO::from);
    }
}
