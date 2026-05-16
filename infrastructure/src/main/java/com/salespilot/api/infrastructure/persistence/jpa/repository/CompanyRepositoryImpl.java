package com.salespilot.api.infrastructure.persistence.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import com.salespilot.api.infrastructure.persistence.jpa.entity.SubscriptionPlans;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CompanyMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.CompanySpecification;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    private final CompanyMapper mapper;
    private final CompanyJpaRepository companyJpaRepository;
    private final SubscriptionPlansJpaRepository subscriptionPlansJpaRepository;
    private final CompanySubscriptionsJpaRepository companySubscriptionsJpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository,
                                 CompanyMapper mapper,
                                 SubscriptionPlansJpaRepository subscriptionPlansJpaRepository,
                                 CompanySubscriptionsJpaRepository companySubscriptionsJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
        this.mapper = mapper;
        this.subscriptionPlansJpaRepository = subscriptionPlansJpaRepository;
        this.companySubscriptionsJpaRepository = companySubscriptionsJpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Company> getAllCompanies(String name, String taxId, String plan, Boolean active, Pageable pageable) {
        Specification<CompanyEntity> spec = Specification
                .where(CompanySpecification.nameLike(name))
                .and(CompanySpecification.taxIdEquals(taxId))
                .and(CompanySpecification.planEquals(plan))
                .and(CompanySpecification.isActiveEquals(active));

        return companyJpaRepository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Company> getCompanyById(UUID id) {
        return companyJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Transactional
    @Override
    public Optional<Company> updateCompany(UUID id, String name, String plan, boolean active) {
        return companyJpaRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    entity.setActive(active);
                    companyJpaRepository.save(entity);
                    updateSubscription(entity, plan);
                    return mapper.toDomain(entity);
                });
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return companyJpaRepository.findByTaxId(taxId).isPresent();
    }

    @Transactional
    @Override
    public Company createCompany(String name, String taxId, String plan, boolean active) {
        CompanyEntity entity = companyJpaRepository.saveAndFlush(new CompanyEntity(name, taxId, active));
        CompanySubscriptions subscription = createSubscription(entity, plan);
        entity.getSubscriptions().add(subscription);
        return mapper.toDomain(entity);
    }

    private CompanySubscriptions createSubscription(CompanyEntity company, String planName) {
        SubscriptionPlans plan = subscriptionPlansJpaRepository
                .findByNameIgnoreCase(planName)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + planName));

        CompanySubscriptions subscription = new CompanySubscriptions();
        subscription.setId(UUID.randomUUID());
        subscription.setCompany(company);
        subscription.setSubscriptionPlans(plan);
        subscription.setActive(true);
        subscription.setStartsAt(LocalDateTime.now());
        subscription.setCreatedAt(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());
        return companySubscriptionsJpaRepository.save(subscription);
    }

    private void updateSubscription(CompanyEntity company, String planName) {
        SubscriptionPlans newPlan = subscriptionPlansJpaRepository
                .findByNameIgnoreCase(planName)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + planName));

        companySubscriptionsJpaRepository.findFirstByCompanyAndActiveTrue(company)
                .ifPresentOrElse(
                        existing -> {
                            if (!existing.getSubscriptionPlans().getName().equalsIgnoreCase(planName)) {
                                existing.setActive(false);
                                existing.setUpdatedAt(LocalDateTime.now());
                                companySubscriptionsJpaRepository.save(existing);
                                createNewActiveSubscription(company, newPlan);
                            }
                        },
                        () -> createNewActiveSubscription(company, newPlan)
                );
    }

    private void createNewActiveSubscription(CompanyEntity company, SubscriptionPlans plan) {
        CompanySubscriptions subscription = new CompanySubscriptions();
        subscription.setId(UUID.randomUUID());
        subscription.setCompany(company);
        subscription.setSubscriptionPlans(plan);
        subscription.setActive(true);
        subscription.setStartsAt(LocalDateTime.now());
        subscription.setCreatedAt(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());
        companySubscriptionsJpaRepository.save(subscription);
    }

    @Override
    public List<Object[]> countCompaniesGroupedByStatus() {
        return companyJpaRepository.countCompaniesGroupedByStatus();
    }    
}
