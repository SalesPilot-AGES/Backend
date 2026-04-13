package com.salespilot.api.infrastructure.persistence.jpa.repository;

import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.enums.CompanyPlan;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CompanyMapper;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyMapper companyMapper;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository, CompanyMapper companyMapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return companyJpaRepository.findByTaxId(taxId).isPresent();
    }

    @Override
    public Company createCompany(String name, String taxId, CompanyPlan companyPlan, boolean active) {
        CompanyEntity entity = new CompanyEntity(
            name,
            taxId,
            companyPlan,
            active
        );

        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return companyMapper.toDomainEntity(savedEntity);
    }
}
