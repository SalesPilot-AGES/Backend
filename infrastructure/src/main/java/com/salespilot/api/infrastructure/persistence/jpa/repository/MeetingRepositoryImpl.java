package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import com.salespilot.api.application.dto.AverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.domain.entity.Meeting;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public List<AverageMeetingDurationPerMonthResponseDTO> groupAverageMeetingDurationPerMonth(
            LocalDateTime start,
            LocalDateTime end
    ) {

        List<Object[]> rows = meetingsJpaRepository.findAverageDurationPerMonth(start, end);

        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        return rows.stream()
                .map(row -> {

                    LocalDateTime month = extractMonth(row[0]);

                    Double avgMinutes = row[1] != null
                            ? ((Number) row[1]).doubleValue()
                            : 0.0;

                    String monthLabel = month != null
                            ? getMonthLabel(month.getMonthValue())
                            : null;

                    return new AverageMeetingDurationPerMonthResponseDTO(
                            month,
                            monthLabel,
                            avgMinutes
                    );
                })
                .toList();
    }

    private LocalDateTime extractMonth(Object value) {

        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }

        return null;
    }

    private String getMonthLabel(int month) {
        return switch (month) {
            case 1 -> "Jan";
            case 2 -> "Fev";
            case 3 -> "Mar";
            case 4 -> "Abr";
            case 5 -> "Mai";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Ago";
            case 9 -> "Set";
            case 10 -> "Out";
            case 11 -> "Nov";
            case 12 -> "Dez";
            default -> "";
        };
    }
}
