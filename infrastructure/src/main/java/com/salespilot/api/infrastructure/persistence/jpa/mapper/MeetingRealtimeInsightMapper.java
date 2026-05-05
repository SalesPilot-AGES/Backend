package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingRealtimeInsight;
import com.salespilot.api.domain.enums.RealtimeInsightType;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingRealtimeInsightsEntity;

public class MeetingRealtimeInsightMapper {
    public MeetingRealtimeInsight toDomain(MeetingRealtimeInsightsEntity entity){
        return new MeetingRealtimeInsight(
            entity.getId(),
            entity.getMeeting().getId(),
            entity.getContent(),
            mapType(entity.getType()),
            entity.getDescription(),
            entity.getCreatedAt()
        );
    }

    private RealtimeInsightType mapType(com.salespilot.api.infrastructure.persistence.jpa.entity.RealtimeInsightType type){
        return com.salespilot.api.domain.enums.RealtimeInsightType.valueOf(type.name());
    }
}
