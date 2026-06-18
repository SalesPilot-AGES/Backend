package com.salespilot.api.infrastructure.persistence.jpa.repository;

import com.salespilot.api.domain.entity.Company;
import com.salespilot.api.domain.model.CompanyStatusCount;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanyStatusHistoryEntity;
import com.salespilot.api.infrastructure.persistence.jpa.entity.CompanySubscriptions;
import com.salespilot.api.infrastructure.persistence.jpa.entity.SubscriptionPlans;
import com.salespilot.api.infrastructure.persistence.jpa.mapper.CompanyMapper;
import com.salespilot.api.infrastructure.persistence.jpa.specification.CompanySpecification;
import com.salespilot.api.model.CompanyNameAndTotalMeetings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    private final CompanyMapper mapper;
    private final CompanyJpaRepository companyJpaRepository;
    private final SubscriptionPlansJpaRepository subscriptionPlansJpaRepository;
    private final CompanySubscriptionsJpaRepository companySubscriptionsJpaRepository;
    private final CompanyStatusHistoryJpaRepository companyStatusHistoryJpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository,
                                 CompanyMapper mapper,
                                 SubscriptionPlansJpaRepository subscriptionPlansJpaRepository,
                                 CompanySubscriptionsJpaRepository companySubscriptionsJpaRepository,
                                 CompanyStatusHistoryJpaRepository companyStatusHistoryJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
        this.mapper = mapper;
        this.subscriptionPlansJpaRepository = subscriptionPlansJpaRepository;
        this.companySubscriptionsJpaRepository = companySubscriptionsJpaRepository;
        this.companyStatusHistoryJpaRepository = companyStatusHistoryJpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Company> getAllCompanies(String name, String taxId, String plan, Boolean active, Pageable pageable) {
        Specification<CompanyEntity> spec = Specification
                .where(CompanySpecification.nameLike(name))
                .and(CompanySpecification.taxIdEquals(taxId))
                .and(CompanySpecification.planEquals(plan))
                .and(CompanySpecification.isActiveEquals(active));

        Page<CompanyEntity> page = companyJpaRepository.findAll(spec, pageable);

        List<UUID> ids = page.getContent().stream().map(CompanyEntity::getId).toList();

        Map<UUID, Object[]> statsMap = fetchStatsMap(ids);
        return page.map(entity -> enrichWithStats(entity, statsMap));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Company> getCompanyById(UUID id) {
        return companyJpaRepository.findById(id).map(entity -> {
            Map<UUID, Object[]> statsMap = fetchStatsMap(List.of(id));
            return enrichWithStats(entity, statsMap);
        });
    }

    private Company enrichWithStats(CompanyEntity entity, Map<UUID, Object[]> statsMap) {
        Object[] stats = statsMap.get(entity.getId());
        long totalMeetings = stats != null ? ((Number) stats[3]).longValue() : 0L;
        long totalCollaborators = stats != null ? ((Number) stats[1]).longValue() : 0L;
        long totalManagers = stats != null ? ((Number) stats[2]).longValue() : 0L;

        return mapper.toDomain(entity)
                .withTotalMeetings(totalMeetings)
                .withTotalCollaborators(totalCollaborators)
                .withTotalManagers(totalManagers);
    }

    private Map<UUID, Object[]> fetchStatsMap(List<UUID> ids) {
        return companyJpaRepository.getStatsByCompanyIds(ids)
                .stream()
                .collect(Collectors.toMap(row -> UUID.fromString(row[0].toString()), row -> row));
    }

    @Transactional
    @Override
    public Optional<Company> updateCompany(UUID id, String name, String plan, boolean active) {
        return companyJpaRepository.findById(id)
                .map(entity -> {
                    boolean previousActive = entity.isActive();
                    entity.setName(name);
                    entity.setActive(active);
                    companyJpaRepository.save(entity);
                    if (previousActive != active) {
                        companyStatusHistoryJpaRepository.save(new CompanyStatusHistoryEntity(entity, active));
                    }
                    updateSubscription(entity, plan);

                    Map<UUID, Object[]> statsMap = fetchStatsMap(List.of(id));
                    return enrichWithStats(entity, statsMap);
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
        companyStatusHistoryJpaRepository.save(new CompanyStatusHistoryEntity(entity, active));
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
    public List<CompanyNameAndTotalMeetings> getTopFiveCompaniesByMeetingTotal(LocalDateTime start, LocalDateTime end) {
        return companyJpaRepository.getTopFiveCompaniesByMeetingTotal(start, end).stream().map(this::mapToNameAndTotalMeetings).toList();
    }

    private CompanyNameAndTotalMeetings mapToNameAndTotalMeetings(Object[] item){
        String name = item[0].toString();
        Long total = ((Number) item[1]).longValue();

        return new CompanyNameAndTotalMeetings(name, total);
    }

    
    @Transactional(readOnly = true)
    @Override
    public List<CompanyStatusCount> countCompaniesGroupedByStatus() {
        return companyJpaRepository
            .countCompaniesGroupedByStatus()
            .stream()
            .map(this::mapToCompanyStatusCount)
            .toList();
    }

    private CompanyStatusCount mapToCompanyStatusCount(Object[] item) {
        if (item == null || item.length < 2) {
            throw new IllegalStateException("Resultado inválido da query de companies");
        }

        Boolean active = (Boolean) item[0];
        Number total = (Number) item[1];

        return new CompanyStatusCount(
            Boolean.TRUE.equals(active),
            total.longValue()
        );
    }    
    
    @Transactional(readOnly = true)
    @Override
    public Long countCompaniesByActiveValueAndPeriod(boolean active, LocalDateTime period) {
        return companyStatusHistoryJpaRepository.countActiveSnapshotAt(active, period);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countCompaniesByActiveValue(boolean active) {
        Specification<CompanyEntity> spec = Specification
            .where(CompanySpecification.isActiveEquals(active));
        return companyJpaRepository.count(spec);
    }
}
