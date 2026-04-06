package com.salespilot.api.infrastructure.persistence.jpa.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.salespilot.api.domain.entity.Enterprise;
import com.salespilot.api.domain.repository.EnterpriseRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.EnterpriseEntity;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.EnterpriseMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.EnterpriseSpecification;

@Repository
public class EnterpriseRepositoryImpl implements EnterpriseRepository {

    private final EnterpriseMapper mapper;
    private final EnterpriseJpaRepository enterpriseJpaRepository;

    public EnterpriseRepositoryImpl(EnterpriseJpaRepository enterpriseJpaRepository, EnterpriseMapper mapper){
        this.enterpriseJpaRepository = enterpriseJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<Enterprise> getAllEnterprises(String nome, String cnpj, String plano, Boolean isActive, Pageable pageable) {
        Specification<EnterpriseEntity> spec = Specification
        .where(EnterpriseSpecification.nomeLike(nome))
        .and(EnterpriseSpecification.cnpjEquals(cnpj))
        .and(EnterpriseSpecification.planoEquals(plano))
        .and(EnterpriseSpecification.isActiveEquals(isActive));

        return enterpriseJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }
}
