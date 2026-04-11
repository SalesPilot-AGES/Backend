package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import com.salespilot.api.infrastructure.persistence.jpa.mapper.CompanyMapper;
import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyMapper companyMapper;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository, CompanyMapper companyMapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public Optional<Company> getCompanyById(UUID id) {
        return companyJpaRepository.findByIdWithCollaborators(id)
                .map(companyMapper::toDomain);
    }
}