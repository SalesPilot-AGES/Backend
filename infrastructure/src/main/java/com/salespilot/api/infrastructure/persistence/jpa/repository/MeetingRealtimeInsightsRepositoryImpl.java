package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingRealtimeInsightMapper;

public class MeetingRealtimeInsightsRepositoryImpl implements MeetingRealtimeInsightRepository{
    private MeetingRealtimeInsightsJpaRepository repository;
    private MeetingRealtimeInsightMapper mapper;

    public MeetingRealtimeInsightsRepositoryImpl(MeetingRealtimeInsightsJpaRepository repository, MeetingRealtimeInsightMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<MeetingRealtimeInsight> findByMeetingId(UUID meetingId, Pageable pageable) {
        return repository.findById(meetingId, pageable).map(mapper::toDomain);
    }
    
}
