package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CompanyMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.CompanySpecification;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    private final CompanyMapper mapper;
    private final CompanyJpaRepository companyJpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository, CompanyMapper mapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return companyJpaRepository.existsByTaxId(taxId);
    }

    @Override
    public Company createCompany(String name, String taxId, String status, Integer maxSellers, Integer maxManagers, String notes) {
        CompanyEntity entity = new CompanyEntity();
        entity.setName(name);
        entity.setTaxId(taxId);
        entity.setStatus(status);
        entity.setMaxSellers(maxSellers);
        entity.setMaxManagers(maxManagers);
        entity.setNotes(notes);
        OffsetDateTime now = OffsetDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Company> getAllCompanies(String name, String taxId, String status, Pageable pageable) {
        Specification<CompanyEntity> spec = Specification
                .where(CompanySpecification.nameLike(name))
                .and(CompanySpecification.taxIdEquals(taxId))
                .and(CompanySpecification.statusEquals(status));

        return companyJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Company> getCompanyById(UUID id) {
        return companyJpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}
