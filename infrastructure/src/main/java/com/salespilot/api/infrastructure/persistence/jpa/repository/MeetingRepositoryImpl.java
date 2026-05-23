package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.model.MonthAndTotal;
import com.salespilot.api.domain.repository.MeetingRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.MeetingMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.MeetingSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Meeting> getLatestMeetingByCollaborator(UUID collaboratorId) {
        Specification<MeetingEntity> spec = MeetingSpecification.collaboratorIdEquals(collaboratorId);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "startedAt"));
        return meetingsJpaRepository.findAll(spec, pageable).getContent().stream()
                .map(mapper::toDomain)
                .findFirst();
    }

    @Override
    public boolean existsById(UUID id){
        return meetingsJpaRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MonthAndTotal> getMeetingsGroupedByMonth(LocalDateTime start, LocalDateTime end) {
        return meetingsJpaRepository.getMeetingsGroupedByMonth(start, end).stream().map(this::mapToMonthAndTotal).toList();
    }

    private MonthAndTotal mapToMonthAndTotal(Object[] item) {
        Object rawMonth = item[0];

        LocalDate month;

        switch (rawMonth) {
            case Timestamp timestamp: 
                month = timestamp.toLocalDateTime().toLocalDate();
                break;
            case LocalDateTime localDateTime:
                month = localDateTime.toLocalDate();
                break;
            case LocalDate localDate:
                month = localDate;
                break;
            default:
                throw new IllegalStateException("Tipo inesperado para month: " + rawMonth.getClass());
        }
        
        Long total = ((Number) item[1]).longValue();
        String monthLabel = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR")).replace(".", "");

        return new MonthAndTotal(
            month,
            capitalize(monthLabel),
            total
        );
    }

    private String capitalize(String monthLabel) {
        return monthLabel.substring(0, 1).toUpperCase() + monthLabel.substring(1);
    }

    @Override
    public Long countTotalMeetingsByPeriod(LocalDateTime currentStart, LocalDateTime currentEnd) {
        return meetingsJpaRepository.countByCreatedAtBetween(currentStart, currentEnd);
    }
}
