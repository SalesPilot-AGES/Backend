package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import com.salespilot.api.domain.repository.MeetingPreAnalysisRepository;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingPreAnalysisMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MeetingPreAnalysisRepositoryImpl implements MeetingPreAnalysisRepository {
    private MeetingPreAnalysisJpaRepository meetingPreAnalysisJpaRepository;
    private MeetingPreAnalysisMapper mapper;

    public MeetingPreAnalysisRepositoryImpl(MeetingPreAnalysisJpaRepository meetingPreAnalysisJpaRepository, MeetingPreAnalysisMapper mapper) {
        this.meetingPreAnalysisJpaRepository = meetingPreAnalysisJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<MeetingPreAnalysis> findByMeetingId(UUID meetingId) {
        return meetingPreAnalysisJpaRepository.findByMeetingId(meetingId)
                .map(mapper::toDomain);
    }
}
