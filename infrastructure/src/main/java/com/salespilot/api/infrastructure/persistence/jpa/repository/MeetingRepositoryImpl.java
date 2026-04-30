package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import com.salespilot.api.infrastructure.persistence.jpa.specification.MeetingSpecification;

public class MeetingRepositoryImpl implements MeetingRepository{
    private final MeetingsJpaRepository meetingsJpaRepository;

    public MeetingRepositoryImpl(MeetingsJpaRepository meetingsJpaRepository) {
        this.meetingsJpaRepository = meetingsJpaRepository;
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
    
}
