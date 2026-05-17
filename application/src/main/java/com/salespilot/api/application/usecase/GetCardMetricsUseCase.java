package com.salespilot.api.application.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import com.salespilot.api.application.dto.CardMetricsResponseDTO;
import com.salespilot.api.application.dto.GroupCardMetricsResponseDTO;
import com.salespilot.api.application.exception.InvalidPeriodException;
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
        LocalDateTime currentStart;
        LocalDateTime currentEnd;
        LocalDateTime previousStart;
        LocalDateTime previousEnd;
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);

        switch (period) {
            case "7d":
                currentStart = today
                    .minusDays(6)
                    .atStartOfDay();
                currentEnd = today.atTime(LocalTime.MAX);
                previousStart = currentStart.minusDays(7);
                previousEnd = currentEnd.minusDays(7);
                break;
            case "30d":      
                currentStart = today
                    .minusDays(29)
                    .atStartOfDay();
                currentEnd = today.atTime(LocalTime.MAX);
                previousStart = currentStart.minusDays(30);
                previousEnd = currentEnd.minusDays(30);
                break;
            case "all":
                currentStart = today
                    .withDayOfMonth(1)
                    .atStartOfDay();
                currentEnd = today.atTime(LocalTime.MAX);  
                previousStart = previousMonth
                        .withDayOfMonth(1)
                        .atStartOfDay();
                int previousDay =
                        Math.min(
                                today.getDayOfMonth(),
                                previousMonth.lengthOfMonth()
                        );
                previousEnd = previousMonth
                        .withDayOfMonth(previousDay)
                        .atTime(LocalTime.MAX);

                break;
            case "custom":
                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException(
                            "startDate and endDate are required for custom period"
                    );
                }

                if (startDate.isAfter(endDate)) {
                    throw new IllegalArgumentException(
                            "startDate cannot be after endDate"
                    );
                }

                currentStart = startDate.atStartOfDay();
                currentEnd = endDate.atTime(LocalTime.MAX);

                long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

                previousStart = currentStart.minusDays(days);
                previousEnd = currentEnd.minusDays(days);
                break;
            default:
                throw new InvalidPeriodException(period);
        }

        Long currentActiveCompanies = companyRepository.countCompaniesByActiveValueAndPeriod(true, currentEnd);
        Long previousActiveCompanies = companyRepository.countCompaniesByActiveValueAndPeriod(true, previousEnd);
        Double activeCompaniesVariationPercent = calculateVariationPercent(previousActiveCompanies, currentActiveCompanies);
        MetricsTrends activeCompaniesTrend = getTrendFromVariationPercent(activeCompaniesVariationPercent);

        Long currentInactiveCompanies = companyRepository.countCompaniesByActiveValueAndPeriod(false, currentEnd);
        Long previousInactiveCompanies = companyRepository.countCompaniesByActiveValueAndPeriod(false, previousEnd);
        Double inactiveCompaniesVariationPercent = calculateVariationPercent(previousInactiveCompanies, currentInactiveCompanies);
        MetricsTrends inactiveCompaniesTrend = getTrendFromVariationPercent(inactiveCompaniesVariationPercent);

        Long currentMeetings = meetingRepository.getTotalMeetingsByPeriod(currentStart, currentEnd);
        Long previousMeetings = meetingRepository.getTotalMeetingsByPeriod(previousStart, previousEnd);
        Double meetingsVariationPercent = calculateVariationPercent(previousMeetings, currentMeetings);
        MetricsTrends meetingsTrend = getTrendFromVariationPercent(meetingsVariationPercent);

        Long currentActiveSellers = collaboratorRepository.getAllActiveSellersByPeriod(currentEnd);
        Long previousActiveSellers = collaboratorRepository.getAllActiveSellersByPeriod(previousEnd);
        Double activeSellersVariationPercent = calculateVariationPercent(previousActiveSellers, currentActiveSellers);
        MetricsTrends activeSellersTrend = getTrendFromVariationPercent(activeSellersVariationPercent);

        CardMetricsResponseDTO activeCompaniesCard = new CardMetricsResponseDTO(currentActiveCompanies, activeCompaniesVariationPercent, activeCompaniesTrend.getValue());
        CardMetricsResponseDTO inactiveCompaniesCard = new CardMetricsResponseDTO(currentInactiveCompanies, inactiveCompaniesVariationPercent, inactiveCompaniesTrend.getValue());
        CardMetricsResponseDTO totalMeetingsCard = new CardMetricsResponseDTO(currentMeetings, meetingsVariationPercent, meetingsTrend.getValue());
        CardMetricsResponseDTO activeSellersCard = new CardMetricsResponseDTO(currentActiveSellers, activeSellersVariationPercent, activeSellersTrend.getValue());
        
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
}