package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.repository.MeetingRealtimeInsightRepository;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingRealtimeInsightMapper;

@Repository
public class MeetingRealtimeInsightsRepositoryImpl implements MeetingRealtimeInsightRepository{
    private final MeetingRealtimeInsightsJpaRepository repository;
    private final MeetingRealtimeInsightMapper mapper;

    public MeetingRealtimeInsightsRepositoryImpl(MeetingRealtimeInsightsJpaRepository repository, MeetingRealtimeInsightMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<MeetingRealtimeInsight> findByMeetingId(UUID meetingId) {
        return repository.findByMeetingIdOrderByCreatedAtDesc(meetingId).stream().map(mapper::toDomain).toList();
    }
    
}
