package com.salespilot.api.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salespilot.api.domain.entity.Company;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    boolean existsByTaxId(String taxId);
    Company createCompany(String name, String taxId, String plan, boolean active);
    Page<Company> getAllCompanies(String name, String taxId, String plan, Boolean active, Pageable pageable);
    Optional<Company> getCompanyById(UUID id);
    Optional<Company> updateCompany(UUID id, String name, String plan, boolean active);
    List<Object[]> getTopFiveCompaniesByMeetingTotal(LocalDateTime start, LocalDateTime end);
}
