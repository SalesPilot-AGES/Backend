package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import org.springframework.stereotype.Component;

import com.salespilot.api.domain.entity.Enterprise;
import com.salespilot.api.infrastructure.persistence.jpa.entity.EnterpriseEntity;

@Component
public class EnterpriseMapper {

    public Enterprise toDomain(EnterpriseEntity enterpriseEntity){
        if (enterpriseEntity == null) {
            return null;
        }
        Enterprise enterprise = new Enterprise(enterpriseEntity.getId(), enterpriseEntity.getNome(), enterpriseEntity.getCnpj(), enterpriseEntity.getPlano(), enterpriseEntity.getIsActive(), enterpriseEntity.getCreated_at());
        return enterprise;
    }
}
