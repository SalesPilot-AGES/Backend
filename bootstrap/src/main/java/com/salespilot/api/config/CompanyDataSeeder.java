package com.salespilot.api.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.repository.CompanyJpaRepository;

@Component
@Order(1)
@ConditionalOnProperty(name = "app.seed.company.enabled", havingValue = "true", matchIfMissing = true)
public class CompanyDataSeeder implements CommandLineRunner {

    private final CompanyJpaRepository companyRepository;

    public CompanyDataSeeder(CompanyJpaRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) {
        seedIfMissing("Maria Santos", "02345678925", true, LocalDateTime.of(2024, 2, 16, 0, 0));
        seedIfMissing("Sandra Castro", "89534956207", true, LocalDateTime.of(2024, 7, 2, 0, 0));
        seedIfMissing("Ana Silva", "45879263102", true, LocalDateTime.of(2024, 9, 7, 0, 0));
    }

    private void seedIfMissing(String name, String taxId, boolean active, LocalDateTime createdAt) {
        if (companyRepository.existsByTaxId(taxId)) {
            return;
        }

        CompanyEntity company = new CompanyEntity(null, name, taxId, active, createdAt);
        companyRepository.save(company);
    }
}


