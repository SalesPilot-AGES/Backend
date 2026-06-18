package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.infrastructure.InfrastructureTestApplication;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = InfrastructureTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Testcontainers
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:db/migration",
        "spring.flyway.schemas=public,auth",
        "spring.flyway.create-schemas=true",
        "spring.jpa.properties.hibernate.default_schema=public"
})
class CollaboratorRepositoryImplTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    CollaboratorRepositoryImpl collaboratorRepository;

    @Autowired
    CompanyJpaRepository companyJpaRepository;

    @Autowired
    CollaboratorJpaRepository collaboratorJpaRepository;

    // ---------------------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------------------

    private CompanyEntity createCompany(String taxId) {
        return companyJpaRepository.saveAndFlush(new CompanyEntity("Test Corp", taxId, true));
    }

    // ---------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------

    @Test
    void shouldCreateAndFindCollaboratorById() {
        CompanyEntity company = createCompany("11.111.111/0001-11");

        Collaborator created = collaboratorRepository.create(
                company.getId(),
                "Ana Silva",
                "ana@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );

        Optional<Collaborator> found = collaboratorRepository.getCollaboratorById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ana Silva");
        assertThat(found.get().getRole()).isEqualTo(CollaboratorRole.SELLER);
        assertThat(found.get().isActive()).isTrue();
    }

    @Test
    void shouldReturnEmptyWhenCollaboratorNotFound() {
        Optional<Collaborator> result = collaboratorRepository.getCollaboratorById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenEmailExistsInCompany() {
        CompanyEntity company = createCompany("22.222.222/0002-22");

        collaboratorRepository.create(
                company.getId(),
                "Ana Silva",
                "ana@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );

        boolean exists = collaboratorRepository.existsByCompanyIdAndEmail(company.getId(), "ana@test.com");

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailNotInCompany() {
        CompanyEntity company = createCompany("33.333.333/0003-33");

        boolean exists = collaboratorRepository.existsByCompanyIdAndEmail(company.getId(), "unknown@test.com");

        assertThat(exists).isFalse();
    }

    @Test
    void shouldFilterManagersByNameLike() {
        CompanyEntity company = createCompany("44.444.444/0004-44");

        collaboratorRepository.create(
                company.getId(),
                "Carlos Manager",
                "carlos.manager@test.com",
                CollaboratorRole.MANAGER,
                true,
                null,
                null
        );
        collaboratorRepository.create(
                company.getId(),
                "Ana Manager",
                "ana.manager@test.com",
                CollaboratorRole.MANAGER,
                true,
                null,
                null
        );

        Page<Collaborator> result = collaboratorRepository.getManagers(
                "carlos", null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Carlos Manager");
    }

    @Test
    void shouldFilterSellersByActive() {
        CompanyEntity company = createCompany("55.555.555/0005-55");

        collaboratorRepository.create(
                company.getId(),
                "Active Seller",
                "active.seller@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );
        collaboratorRepository.create(
                company.getId(),
                "Inactive Seller",
                "inactive.seller@test.com",
                CollaboratorRole.SELLER,
                false,
                null,
                null
        );

        Page<Collaborator> result = collaboratorRepository.getSellers(
                null, null, null, true, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Active Seller");
        assertThat(result.getContent().get(0).isActive()).isTrue();
    }

    @Test
    void shouldCountAllActiveSellers() {
        CompanyEntity company = createCompany("66.666.666/0006-66");

        collaboratorRepository.create(
                company.getId(),
                "Active Seller One",
                "seller1@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );
        collaboratorRepository.create(
                company.getId(),
                "Active Seller Two",
                "seller2@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );
        collaboratorRepository.create(
                company.getId(),
                "Inactive Seller Three",
                "seller3@test.com",
                CollaboratorRole.SELLER,
                false,
                null,
                null
        );

        Long count = collaboratorRepository.countAllActiveSellers();

        assertThat(count).isEqualTo(2L);
    }

    @Test
    void shouldUpdateCollaboratorFields() {
        CompanyEntity company = createCompany("77.777.777/0007-77");

        Collaborator created = collaboratorRepository.create(
                company.getId(),
                "Original Name",
                "original@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );

        collaboratorRepository.update(
                company.getId(),
                created.getId(),
                "Updated Name",
                "updated@test.com",
                "11999999999",
                true,
                null
        );

        Optional<Collaborator> found = collaboratorRepository.getCollaboratorById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Name");
        assertThat(found.get().getEmail()).isEqualTo("updated@test.com");
        assertThat(found.get().getPhone()).isEqualTo("11999999999");
    }

    @Test
    void shouldUpdatePasswordHash() {
        CompanyEntity company = createCompany("88.888.888/0008-88");

        Collaborator created = collaboratorRepository.create(
                company.getId(),
                "Hash Tester",
                "hash@test.com",
                CollaboratorRole.SELLER,
                true,
                null,
                null
        );

        collaboratorRepository.updatePasswordHash(created.getId(), "newHashedPassword");

        Optional<com.salespilot.api.infrastructure.persistence.jpa.entity.CollaboratorEntity> entity =
                collaboratorJpaRepository.findById(created.getId());

        assertThat(entity).isPresent();
        assertThat(entity.get().getPasswordHash()).isEqualTo("newHashedPassword");
    }
}
