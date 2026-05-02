package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.valueobject.PreAnalysisKeyPoints;
import com.salespilot.api.domain.valueobject.PreAnalysisPossibleObjections;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPreAnalysisEntity;
import org.springframework.stereotype.Component;

@Component
public class MeetingPreAnalysisMapper {
    public MeetingPreAnalysis toDomain(MeetingPreAnalysisEntity entity) {
        return new MeetingPreAnalysis(
                entity.getId(),
                entity.getMeeting().getId(),
                entity.getPreAnalysisRecommendedStrategy(),
                new PreAnalysisKeyPoints(entity.getPreAnalysisKeyPoints()),
                new PreAnalysisPossibleObjections(entity.getPreAnalysisPossibleObjections()),
                entity.getCreatedAt()
        );
    }
}
