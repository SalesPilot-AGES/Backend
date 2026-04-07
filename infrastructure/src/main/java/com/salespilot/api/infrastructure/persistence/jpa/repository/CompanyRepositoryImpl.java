package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.salespilot.api.application.exception.CnpjAlreadyExists;
import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CompanyMapper;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Company createCompany(String nome, String cnpj, String plano, boolean is_active) {
        
        if(companyJpaRepository.findByCnpj(cnpj).isPresent()) {
            throw new CnpjAlreadyExists();
        }
        
        CompanyEntity entity = new CompanyEntity(
            UUID.randomUUID(),
            nome,
            cnpj,
            plano,
            is_active,
            new Timestamp(System.currentTimeMillis())
        );
        
        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return CompanyMapper.toDomainEntity(savedEntity);
    }
    
}
