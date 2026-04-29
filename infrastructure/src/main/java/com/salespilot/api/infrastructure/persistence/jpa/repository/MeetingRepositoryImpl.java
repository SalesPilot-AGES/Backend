package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.MeetingSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.OptionalDouble;
import java.util.UUID;

@Repository
public class MeetingRepositoryImpl implements MeetingRepository {
    private final MeetingsJpaRepository meetingsJpaRepository;
    private final MeetingMapper mapper;

    public MeetingRepositoryImpl(MeetingsJpaRepository meetingsJpaRepository, MeetingMapper mapper) {
        this.meetingsJpaRepository = meetingsJpaRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorID, Pageable pageable) {
        Specification<Meetings> spec = Specification
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
        return meetingsJpaRepository.findAll()
                .stream()
                .map(Meetings::getDurationSeconds)
                .filter(d -> d != null)
                .mapToLong(Long::valueOf)
                .average()
                .orElse(0);
    }

    public long getTotalMeetingsByCollaboratorId(UUID collaboratorId) {
        Specification<Meetings> spec = Specification
                .where(MeetingSpecification.collaboratorIdEquals(collaboratorId));

        return meetingsJpaRepository.count(spec);
    }

    public double getAverageDurationSecondsByCollaboratorId(UUID collaboratorId) {
        Specification<Meetings> spec = Specification
                .where(MeetingSpecification.collaboratorIdEquals(collaboratorId));

        return meetingsJpaRepository.findAll(spec)
                .stream()
                .map(Meetings::getDurationSeconds)
                .filter(d -> d != null)
                .mapToLong(Long::valueOf)
                .average()
                .orElse(0);
    }
/*
    @Override
    public Optional<Meeting> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Meeting> findByCollaboratorId(UUID collaboratorId) {
        return List.of();
    }

    @Override
    public List<Meeting> findByClientId(UUID clientId) {
        return List.of();
    }

    @Override
    public Meeting save(Meeting meeting) {
        return null;
    }*/


}
