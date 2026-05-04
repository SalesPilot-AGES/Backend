package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.MeetingSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository

public class MeetingRepositoryImpl implements MeetingRepository{
    private final MeetingsJpaRepository meetingsJpaRepository;
    private final MeetingMapper mapper;


    public MeetingRepositoryImpl(MeetingsJpaRepository meetingsJpaRepository, MeetingMapper mapper) {
        this.meetingsJpaRepository = meetingsJpaRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Long getTotalMeetingsByCollaborator(UUID collaboratorId) {
        Specification<MeetingEntity> spec = Specification.where(
            MeetingSpecification.collaboratorIdEquals(collaboratorId)
        );
        return meetingsJpaRepository.count(spec);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable) {
        Specification<MeetingEntity> spec = Specification
                .where(MeetingSpecification.titleLike(title)
                .and(MeetingSpecification.clientCompanyNameLike(clientCompanyName)
                .and(MeetingSpecification.collaboratorIdEquals(collaboratorID)))
                .and(MeetingSpecification.collaboratorIsActive(collaboratorID)));

        return meetingsJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    public long getTotalMeetings() {
        return meetingsJpaRepository.count();
    }

    public double getAverageDurationSeconds() {
        return meetingsJpaRepository.findAverageDurationSeconds().orElse(0.0);
    }

    @Override
    public Optional<Meeting> getMeetingById(UUID id) {
        return meetingsJpaRepository.findById(id).map(mapper::toDomain);
    }
}
