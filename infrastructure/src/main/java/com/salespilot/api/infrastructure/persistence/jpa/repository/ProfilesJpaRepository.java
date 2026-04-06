package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.Profiles;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilesJpaRepository extends JpaRepository<Profiles, UUID> {
}
