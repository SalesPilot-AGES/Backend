package com.salespilot.api.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.repository.CollaboratorJpaRepository;
import com.salespilot.api.infrastructure.persistence.jpa.repository.CompanyJpaRepository;

@Component
@Order(2)
@ConditionalOnProperty(name = "app.seed.collaborator.enabled", havingValue = "true", matchIfMissing = true)
public class CollaboratorDataSeeder implements CommandLineRunner {

    private final CompanyJpaRepository companyRepository;
    private final CollaboratorJpaRepository collaboratorRepository;

    public CollaboratorDataSeeder(CompanyJpaRepository companyRepository, CollaboratorJpaRepository collaboratorRepository) {
        this.companyRepository = companyRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Override
    public void run(String... args) {
        seedForCompany("02345678925", "Carlos Mendes", "carlos@company.com", true,
                "{\"language\":\"pt-BR\",\"notifications\":true}", LocalDateTime.of(2024, 3, 1, 10, 0));
        seedForCompany("02345678925", "Fernanda Rocha", "fernanda@company.com", true,
                null, LocalDateTime.of(2024, 3, 5, 14, 30));
        seedForCompany("89534956207", "Joao Batista", "joao@company.com", true,
                "{\"theme\":\"dark\"}", LocalDateTime.of(2024, 8, 10, 9, 15));
    }

    private void seedForCompany(String companyTaxId, String name, String email, boolean active, String preferences,
            LocalDateTime createdAt) {
        CompanyEntity company = companyRepository.findByTaxId(companyTaxId).orElse(null);
        if (company == null || collaboratorRepository.existsByCompanyIdAndEmail(company.getId(), email)) {
            return;
        }

        CollaboratorEntity collaborator = new CollaboratorEntity(company, name, email, active, preferences, createdAt);
        collaboratorRepository.save(collaborator);
    }
}