package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.domain.model.StatusCount;
import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import com.salespilot.api.model.SellerNameAndTotalMeetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollaboratorRepository {
    Collaborator create(UUID companyId, String name, String email, CollaboratorRole role, boolean active, String phone, CollaboratorPreferences preferences);
    Collaborator update(UUID companyId, UUID collaboratorId, String name, String email, String phone, boolean active, CollaboratorPreferences preferences);
    boolean existsByCompanyIdAndEmail(UUID companyId, String email);
    Optional<Collaborator> getCollaboratorById(UUID id);
    Page<Collaborator> getManagers(String name, String email, UUID companyId, Boolean active, Pageable pageable);
    Page<Collaborator> getSellers(String name, String email, UUID companyId, Boolean active, Pageable pageable);
    Long countAllActiveSellersByPeriod(LocalDateTime period);
    Long countAllActiveSellers();
    void updatePasswordHash(UUID collaboratorId, String passwordHash);
    Long countSellersByCompanyIdAndActiveValue(UUID companyId, boolean active);
    Long countActiveSellersByCompanyIdAndPeriod(UUID companyId, LocalDateTime period);
    Long countInactiveSellersByCompanyIdAndPeriod(UUID companyId, LocalDateTime period);
    List<StatusCount> countSellersGroupedByStatus();
    List<SellerNameAndTotalMeetings> getMeetingsBySeller(LocalDateTime start, LocalDateTime end);
}
