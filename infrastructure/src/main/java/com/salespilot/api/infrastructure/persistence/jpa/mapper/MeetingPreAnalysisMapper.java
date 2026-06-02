package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPreAnalysisEntity;
import org.springframework.stereotype.Component;

@Component
public class MeetingPreAnalysisMapper {
    public MeetingPreAnalysis toDomain(MeetingPreAnalysisEntity entity) {
        return new MeetingPreAnalysis(
                entity.getId(),
                entity.getMeeting().getId(),
                entity.getRecommendedStrategy(),
                entity.getKeyPoints(),
                entity.getPossibleObjections(),
                entity.getCreatedAt()
        );
    }
}
