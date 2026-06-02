package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.model.MonthAndTotal;
import com.salespilot.api.domain.model.AverageMeetingDurationPerMonth;
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
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

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
    public Page<Meeting> getAllMeetings(String title, String clientCompanyName, UUID collaboratorId, UUID companyId, Pageable pageable) {
        Specification<MeetingEntity> spec = Specification
                .where(MeetingSpecification.titleLike(title)
                .and(MeetingSpecification.clientCompanyNameLike(clientCompanyName)
                .and(MeetingSpecification.collaboratorIdEquals(collaboratorId)))
                .and(MeetingSpecification.collaboratorIsActive(collaboratorId)))
                .and(MeetingSpecification.companyIdEquals(companyId));

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
    public List<MonthAndTotal> getMeetingsGroupedByMonth(LocalDateTime start, LocalDateTime end, UUID companyId, UUID collaboratorId) {
        return meetingsJpaRepository.getMeetingsGroupedByMonth(start, end, companyId, collaboratorId)
                .stream()
                .map(this::mapToMonthAndTotal)
                .toList();
    }

    private MonthAndTotal mapToMonthAndTotal(Object[] item) {
        LocalDate month = toLocalDateTime(item[0]).toLocalDate();
        Long total = ((Number) item[1]).longValue();

        return new MonthAndTotal(
            month,
            monthLabel(month),
            total
        );
    }

    private String monthLabel(LocalDate month) {
        String label = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.of("pt", "BR")).replace(".", "");
        return capitalize(label);
    }

    private String capitalize(String monthLabel) {
        return monthLabel.substring(0, 1).toUpperCase() + monthLabel.substring(1);
    }

    @Override
    public Long countTotalMeetingsByPeriod(LocalDateTime currentStart, LocalDateTime currentEnd) {
        return meetingsJpaRepository.countByCreatedAtBetween(currentStart, currentEnd);
    }

    @Override
    public Long countTotalMeetingsByCompanyIdAndPeriod(UUID companyId, LocalDateTime start, LocalDateTime end) {
        Specification<MeetingEntity> spec = Specification
                .where(MeetingSpecification.companyIdEquals(companyId))
                .and(MeetingSpecification.createdAtBetween(start, end));

        return meetingsJpaRepository.count(spec);
    }

    @Override
    public Long countTotalMeetingsByCollaboratorIdAndPeriod(UUID collaboratorId, LocalDateTime start, LocalDateTime end) {
        Specification<MeetingEntity> spec = Specification
                .where(MeetingSpecification.collaboratorIdEquals(collaboratorId))
                .and(MeetingSpecification.createdAtBetween(start, end));

        return meetingsJpaRepository.count(spec);
    }

    @Override
    public Double getAverageDurationByCollaboratorIdAndPeriod(UUID collaboratorId, LocalDateTime start, LocalDateTime end) {
        return meetingsJpaRepository
                .findAverageDurationSecondsByCollaboratorAndPeriod(collaboratorId, start, end)
                .orElse(0.0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AverageMeetingDurationPerMonth> groupAverageMeetingDurationPerMonth(
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<Object[]> rows = meetingsJpaRepository.findAverageDurationPerMonth(start, end);

        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        return rows.stream()
                .map(row -> {
                    LocalDateTime month = toLocalDateTime(row[0]);

                    Double avgMinutes = row[1] != null
                            ? ((Number) row[1]).doubleValue()
                            : null;

                    return new AverageMeetingDurationPerMonth(
                            month,
                            monthLabel(month.toLocalDate()),
                            avgMinutes
                    );
                })
                .toList();
    }

    private LocalDateTime toLocalDateTime(Object value) {
        return switch (value) {
            case Timestamp timestamp -> timestamp.toLocalDateTime();
            case LocalDateTime localDateTime -> localDateTime;
            case LocalDate localDate -> localDate.atStartOfDay();
            default -> throw new IllegalStateException("Unexpected type for the month: " + value.getClass());
        };
    }
}
