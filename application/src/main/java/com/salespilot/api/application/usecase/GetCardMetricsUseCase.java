package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.salespilot.api.application.dto.CardMetricsResponseDTO;
import com.salespilot.api.application.dto.GroupCardMetricsResponseDTO;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.enums.MetricsTrends;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingRepository;

public class GetCardMetricsUseCase {
    private final CompanyRepository companyRepository;
    private final MeetingRepository meetingRepository;
    private final CollaboratorRepository collaboratorRepository;

    public GetCardMetricsUseCase(CompanyRepository companyRepository, MeetingRepository meetingRepository, CollaboratorRepository collaboratorRepository) {
        this.companyRepository = companyRepository;
        this.meetingRepository = meetingRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public GroupCardMetricsResponseDTO execute(String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime[] dates = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics(period, startDate, endDate);
        LocalDateTime previousStart = dates[0];
        LocalDateTime previousEnd = dates[1];
        LocalDateTime currentStart = dates[2];
        LocalDateTime currentEnd = dates[3];
        
        Long currentActiveCompanies = companyRepository.countCompaniesByActiveValue(true);
        Long previousActiveCompanies = companyRepository.countCompaniesByActiveValueAndPeriod(true, previousEnd);

        Long currentInactiveCompanies = companyRepository.countCompaniesByActiveValue(false);
        Long previousInactiveCompanies = companyRepository.countCompaniesByActiveValueAndPeriod(false, previousEnd);

        Long currentMeetings = meetingRepository.countTotalMeetingsByPeriod(currentStart, currentEnd);
        Long previousMeetings = meetingRepository.countTotalMeetingsByPeriod(previousStart, previousEnd);

        Long currentActiveSellers = collaboratorRepository.countAllActiveSellers();
        Long previousActiveSellers = collaboratorRepository.countAllActiveSellersByPeriod(previousEnd);

        CardMetricsResponseDTO activeCompaniesCard = buildMetricCard(currentActiveCompanies, previousActiveCompanies);
        CardMetricsResponseDTO inactiveCompaniesCard = buildMetricCard(currentInactiveCompanies, previousInactiveCompanies);
        CardMetricsResponseDTO totalMeetingsCard = buildMetricCard(currentMeetings, previousMeetings);
        CardMetricsResponseDTO activeSellersCard = buildMetricCard(currentActiveSellers, previousActiveSellers);
        
        return new GroupCardMetricsResponseDTO(activeCompaniesCard, inactiveCompaniesCard, totalMeetingsCard, activeSellersCard);
    }

    private Double calculateVariationPercent(Long previous, Long current) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        } else {
            return ((double) (current - previous) / previous) * 100.0; 
        }
    }

    private MetricsTrends getTrendFromVariationPercent(Double variationPercent) {
        if(variationPercent == 0) {
            return MetricsTrends.NEUTRAL;
        } else {
            return variationPercent > 0 ? MetricsTrends.UP : MetricsTrends.DOWN;
        }
    }

    private CardMetricsResponseDTO buildMetricCard(Long current, Long previous) {
        Double variation = calculateVariationPercent(previous, current);
        MetricsTrends trend = getTrendFromVariationPercent(variation);
        return new CardMetricsResponseDTO(current, variation, trend.getValue());
    }
}