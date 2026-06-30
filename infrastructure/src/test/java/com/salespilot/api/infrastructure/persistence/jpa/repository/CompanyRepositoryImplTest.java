package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.model.StatusCount;
import com.salespilot.api.infrastructure.InfrastructureTestApplication;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.SubscriptionPlans;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.List;
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
class CompanyRepositoryImplTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    CompanyRepositoryImpl companyRepository;

    @Autowired
    CompanyJpaRepository companyJpaRepository;

    @Autowired
    SubscriptionPlansJpaRepository subscriptionPlansJpaRepository;

    SubscriptionPlans savedPlan;

    @BeforeEach
    void setUp() {
        savedPlan = new SubscriptionPlans();
        savedPlan.setId(UUID.randomUUID());
        savedPlan.setName("BASIC");
        savedPlan.setDescription("Basic plan");
        savedPlan.setPriceCents(0);
        savedPlan.setStatus("ACTIVE");
        savedPlan.setCreatedAt(LocalDateTime.now());
        subscriptionPlansJpaRepository.saveAndFlush(savedPlan);
    }

    @Test
    void shouldCreateAndFindCompanyById() {
        Company created = companyRepository.createCompany("Acme Corp", "11.111.111/0001-11", "BASIC", true);

        Optional<Company> found = companyRepository.getCompanyById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Acme Corp");
        assertThat(found.get().isActive()).isTrue();
        assertThat(found.get().getPlan()).isEqualTo("BASIC");
    }

    @Test
    void shouldReturnEmptyWhenCompanyNotFound() {
        Optional<Company> found = companyRepository.getCompanyById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFilterByNameContaining() {
        companyJpaRepository.saveAndFlush(new CompanyEntity("Acme Corp", "11.222.333/0001-44", true));
        companyJpaRepository.saveAndFlush(new CompanyEntity("Beta Ltd", "55.666.777/0001-88", true));

        Page<Company> result = companyRepository.getAllCompanies("acme", null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Acme Corp");
    }

    @Test
    void shouldFilterByActive() {
        companyJpaRepository.saveAndFlush(new CompanyEntity("Active Company", "11.000.111/0001-00", true));
        companyJpaRepository.saveAndFlush(new CompanyEntity("Inactive Company", "22.000.222/0001-00", false));

        Page<Company> activeResults = companyRepository.getAllCompanies(null, null, null, true, PageRequest.of(0, 10));

        assertThat(activeResults.getContent()).allMatch(Company::isActive);

        Long inactiveCount = companyRepository.countCompaniesByActiveValue(false);
        assertThat(inactiveCount).isGreaterThanOrEqualTo(1L);
    }

    @Test
    void shouldCountCompaniesByActive() {
        companyJpaRepository.saveAndFlush(new CompanyEntity("Active One", "33.001.001/0001-01", true));
        companyJpaRepository.saveAndFlush(new CompanyEntity("Active Two", "33.002.002/0001-02", true));
        companyJpaRepository.saveAndFlush(new CompanyEntity("Inactive One", "33.003.003/0001-03", false));

        Long activeCount = companyRepository.countCompaniesByActiveValue(true);
        Long inactiveCount = companyRepository.countCompaniesByActiveValue(false);

        assertThat(activeCount).isGreaterThanOrEqualTo(2L);
        assertThat(inactiveCount).isGreaterThanOrEqualTo(1L);
    }

    @Test
    void shouldCheckExistsByTaxId() {
        companyJpaRepository.saveAndFlush(new CompanyEntity("Tax Check Corp", "99.999.999/0001-99", true));

        assertThat(companyRepository.existsByTaxId("99.999.999/0001-99")).isTrue();
        assertThat(companyRepository.existsByTaxId("00.000.000/0000-00")).isFalse();
    }

    @Test
    void shouldUpdateCompanyName() {
        Company created = companyRepository.createCompany("Update Corp", "77.777.777/0007-77", "BASIC", true);

        companyRepository.updateCompany(created.getId(), "Renamed Corp", "BASIC", true);

        Optional<Company> found = companyRepository.getCompanyById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Renamed Corp");
        assertThat(found.get().isActive()).isTrue();
    }

    @Test
    void shouldGroupCompaniesByStatus() {
        companyJpaRepository.saveAndFlush(new CompanyEntity("Active Alpha", "44.001.001/0001-01", true));
        companyJpaRepository.saveAndFlush(new CompanyEntity("Active Beta", "44.002.002/0001-02", true));
        companyJpaRepository.saveAndFlush(new CompanyEntity("Inactive Gamma", "44.003.003/0001-03", false));

        List<StatusCount> result = companyRepository.countCompaniesGroupedByStatus();

        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(entry -> entry.active() && entry.total() >= 2);
        assertThat(result).anyMatch(entry -> !entry.active() && entry.total() >= 1);
    }
}
