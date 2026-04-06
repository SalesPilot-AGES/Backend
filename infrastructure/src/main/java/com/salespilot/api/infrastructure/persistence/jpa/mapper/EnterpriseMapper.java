package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Enterprise;
import com.salespilot.api.infrastructure.persistence.jpa.entity.EnterpriseEntity;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseMapper {
    public static Enterprise toEnterprise(EnterpriseEntity enterpriseEntity) {
        return new Enterprise(enterpriseEntity.getId(), enterpriseEntity.getNome(), enterpriseEntity.getCnpj(), enterpriseEntity.getPlano(), enterpriseEntity.isActive(), enterpriseEntity.getCreatedAt());
    }
}
