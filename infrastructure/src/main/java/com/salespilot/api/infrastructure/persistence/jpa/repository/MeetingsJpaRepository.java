package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MeetingsJpaRepository extends JpaRepository<Meetings, UUID>, JpaSpecificationExecutor<Meetings> {
    @Query("SELECT AVG(m.durationSeconds) FROM Meetings m WHERE m.durationSeconds IS NOT NULL")
    Optional<Double> findAverageDurationSeconds();
}
