package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    private final CollaboratorMapper collaboratorMapper;

    public CompanyMapper(CollaboratorMapper collaboratorMapper) {
        this.collaboratorMapper = collaboratorMapper;
    }

    public Company toDomain(CompanyEntity companyEntity) {
        return new Company(companyEntity.getId(),
                companyEntity.getName(),
                companyEntity.getTaxId(),
                companyEntity.getPlan(),
                companyEntity.isActive(),
                companyEntity.getCreatedAt(),
                collaboratorMapper.toDomainList(companyEntity.getCollaborators()));
    }
}
