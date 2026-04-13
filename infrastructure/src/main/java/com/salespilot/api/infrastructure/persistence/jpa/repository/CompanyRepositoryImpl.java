package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;
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

    @Transactional(readOnly = true)
    @Override
    public Page<Company> getAllCompanies(String name, String taxId, CompanyPlan plan, Boolean active, Pageable pageable) {
        Specification<CompanyEntity> spec = Specification
                .where(CompanySpecification.nameLike(name))
                .and(CompanySpecification.taxIdEquals(taxId))
                .and(CompanySpecification.planEquals(plan))
                .and(CompanySpecification.isActiveEquals(active));

        return companyJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Company> getCompanyById(UUID id) {
        return companyJpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}