package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.MeetingSpecification;

@Repository

public class MeetingRepositoryImpl implements MeetingRepository{
    private final MeetingsJpaRepository meetingsJpaRepository;
    private final MeetingMapper mapper;


    public MeetingRepositoryImpl(MeetingsJpaRepository meetingsJpaRepository, MeetingMapper mapper) {
        this.meetingsJpaRepository = meetingsJpaRepository;
        this.mapper = mapper;
    }

    public Long getTotalMeetings(UUID collaboratorId) {
        Specification<Meetings> spec = Specification.where(
            MeetingSpecification.collaboratorIdEquals(collaboratorId)
        );
        return meetingsJpaRepository.count(spec);
    }

    @Override
    public Optional<Meeting> findById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<Meeting> findByCollaboratorId(UUID collaboratorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCollaboratorId'");
    }

    @Override
    public List<Meeting> findByClientId(UUID clientId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByClientId'");
    }

    @Override
    public Meeting save(Meeting meeting) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
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
        return meetingsJpaRepository.findAverageDurationSeconds().orElse(0.0);
    }
}
