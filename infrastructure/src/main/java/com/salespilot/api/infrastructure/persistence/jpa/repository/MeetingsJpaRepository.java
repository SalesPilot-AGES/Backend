package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Meetings;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingsJpaRepository extends JpaRepository<Meetings, UUID> {
}
