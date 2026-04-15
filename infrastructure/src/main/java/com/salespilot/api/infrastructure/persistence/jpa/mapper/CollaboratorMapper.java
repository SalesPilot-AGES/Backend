package com.salespilot.api.infrastructure.persistence.jpa.mapper;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollaboratorMapper {

    private final CompanyMapper companyMapper;

    public CollaboratorMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    public Collaborator toDomain(CollaboratorEntity collaboratorEntity) {
        return new Collaborator(collaboratorEntity.getId(),
                companyMapper.toDomain(collaboratorEntity.getCompany()),
                collaboratorEntity.getName(),
                collaboratorEntity.getEmail(),
                collaboratorEntity.getRole(),
                collaboratorEntity.isActive(),
                collaboratorEntity.getPreferences(),
                collaboratorEntity.getCreatedAt());
    }

    public List<Collaborator> toDomainList(List<CollaboratorEntity> collaboratorEntityList) {
        return collaboratorEntityList.stream()
                .map(this::toDomain)
                .toList();
    }
} 
