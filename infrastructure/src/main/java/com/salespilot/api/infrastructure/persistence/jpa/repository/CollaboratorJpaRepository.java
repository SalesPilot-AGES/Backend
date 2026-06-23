package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollaboratorJpaRepository extends JpaRepository<CollaboratorEntity, UUID>, JpaSpecificationExecutor<CollaboratorEntity> {
    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
    Optional<CollaboratorEntity> findByEmail(String email);

    @Query("""
    SELECT c.active, COUNT(c)
    FROM CollaboratorEntity c
    WHERE c.role = com.salespilot.api.domain.enums.CollaboratorRole.SELLER
    GROUP BY c.active
""")
    List<Object[]> countSellersGroupedByStatus();
}
