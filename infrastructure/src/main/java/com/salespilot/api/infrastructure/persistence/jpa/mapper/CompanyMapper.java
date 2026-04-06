package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import org.springframework.stereotype.Component;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;

@Component
public class CompanyMapper {

    public Company toDomain(CompanyEntity companyEntity){
        if (companyEntity == null) {
            return null;
        }
        Company company = new Company(companyEntity.getId(), companyEntity.getNome(), companyEntity.getCnpj(), companyEntity.getPlano(), companyEntity.getIsActive(), companyEntity.getCreated_at());
        return company;
    }
}
