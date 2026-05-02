package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.MeetingPostAnalysisEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPostAnalysisJpaRepository extends JpaRepository<MeetingPostAnalysisEntity, UUID> {
}
