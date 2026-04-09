package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.salespilot.api.domain.entity.Company;
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
    public Optional<Company> findById(UUID id) {
        return companyJpaRepository.findById(id)
                .map(companyMapper::toDomain);
    }

    @Override
    public Company save(Company company) {
        CompanyEntity entity = companyMapper.toEntity(company);
        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return companyMapper.toDomain(savedEntity);
    }
}