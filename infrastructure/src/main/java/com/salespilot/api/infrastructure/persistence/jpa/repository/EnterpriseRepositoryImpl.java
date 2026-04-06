package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import com.salespilot.api.infrastructure.persistence.jpa.entity.EnterpriseEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.EnterpriseMapper;
import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entity.Enterprise;
import com.salespilot.api.domain.repository.EnterpriseRepository;

@Repository
public class EnterpriseRepositoryImpl implements EnterpriseRepository {

    private final EnterpriseJpaRepository enterpriseJpaRepository;

    public EnterpriseRepositoryImpl(EnterpriseJpaRepository enterpriseJpaRepository){
        this.enterpriseJpaRepository = enterpriseJpaRepository;
    }


    @Override
    public Enterprise getEnterpriseById(UUID id) {
        Optional<EnterpriseEntity> enterpriseEntity = enterpriseJpaRepository.findById(id);

        if(enterpriseEntity.isPresent()){
            return new EnterpriseMapper().toEnterprise(enterpriseEntity.get());
        }

        return null;
    }
}