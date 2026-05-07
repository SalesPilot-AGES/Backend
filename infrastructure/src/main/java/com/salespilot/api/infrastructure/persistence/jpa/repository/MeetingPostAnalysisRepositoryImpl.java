package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.MeetingPostAnalysis;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingPostAnalysisMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MeetingPostAnalysisRepositoryImpl implements MeetingPostAnalysisRepository {
    private final MeetingPostAnalysisJpaRepository meetingPostAnalysisJpaRepository;
    private final MeetingPostAnalysisMapper mapper;

    public MeetingPostAnalysisRepositoryImpl(MeetingPostAnalysisJpaRepository meetingPostAnalysisJpaRepository, MeetingPostAnalysisMapper mapper) {
        this.meetingPostAnalysisJpaRepository = meetingPostAnalysisJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<MeetingPostAnalysis> findByMeetingId(UUID meetingId) {
        return meetingPostAnalysisJpaRepository.findByMeetingId(meetingId)
                .map(mapper::toDomain);
    }

    @Override
    public double getAverageSuccessRate() {
        return meetingPostAnalysisJpaRepository.findAverageSuccessRate().orElse(0.0);
    }
}
