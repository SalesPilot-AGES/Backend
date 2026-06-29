package com.salespilot.api.application.usecase;

import com.salespilot.api.application.dto.AdminGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.CardMetricsResponseDTO;
import com.salespilot.api.application.dto.GroupCardMetricsResponse;
import com.salespilot.api.application.dto.ManagerGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.SellerGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.SentimentCardMetricsResponseDTO;
import com.salespilot.api.application.utils.DashboardPeriodUtils;
import com.salespilot.api.domain.enums.MetricsTrends;
import com.salespilot.api.domain.repository.CollaboratorRepository;
import com.salespilot.api.domain.repository.CompanyRepository;
import com.salespilot.api.domain.repository.MeetingPostAnalysisRepository;
import com.salespilot.api.domain.repository.MeetingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class GetCardMetricsUseCase {
    private final CompanyRepository companyRepository;
    private final MeetingRepository meetingRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final MeetingPostAnalysisRepository meetingPostAnalysisRepository;

    public GetCardMetricsUseCase(CompanyRepository companyRepository, MeetingRepository meetingRepository, CollaboratorRepository collaboratorRepository, MeetingPostAnalysisRepository meetingPostAnalysisRepository) {
        this.companyRepository = companyRepository;
        this.meetingRepository = meetingRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.meetingPostAnalysisRepository = meetingPostAnalysisRepository;
    }

    public GroupCardMetricsResponse execute(String period, LocalDate startDate, LocalDate endDate, AuthUserDTO authUser) {
        LocalDateTime[] dates = DashboardPeriodUtils.dashboardPeriodUtilsToCardMetrics(period, startDate, endDate);
        LocalDateTime previousStart = dates[0];
        LocalDateTime previousEnd = dates[1];
        LocalDateTime currentStart = dates[2];
        LocalDateTime currentEnd = dates[3];

        return switch (authUser.role()) {
            case SYSTEM_ADMIN -> buildAdminGroupCard(previousStart, previousEnd, currentStart, currentEnd);
            case MANAGER -> buildManagerGroupCard(previousStart, previousEnd, currentStart, currentEnd, authUser.companyId());
            case SELLER -> buildSellerGroupCard(previousStart, previousEnd, currentStart, currentEnd, authUser.id());
        };
    }

    private Double calculateVariationPercent(Long previous, Long current) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        } else {
            return ((double) (current - previous) / previous) * 100.0;
        }
    }

    private Double calculateVariationPercent(Double previous, Double current) {
        if (previous == 0.0) {
            return current > 0.0 ? 100.0 : 0.0;
        } else {
            return ((current - previous) / previous) * 100.0;
        }
    }

    private MetricsTrends getTrendFromVariationPercent(Double variationPercent) {
        if (variationPercent == 0) {
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

    private SentimentCardMetricsResponseDTO buildSentimentCard(Double current, Double previous) {
        Double safeCurrent = current != null ? current : 0.0;
        Double safePrevious = previous != null ? previous : 0.0;
        Double variation = calculateVariationPercent(safePrevious, safeCurrent);
        MetricsTrends trend = getTrendFromVariationPercent(variation);
        return new SentimentCardMetricsResponseDTO(safeCurrent, variation, trend.getValue());
    }

    private AdminGroupCardMetricsResponseDTO buildAdminGroupCard(LocalDateTime previousStart, LocalDateTime previousEnd, LocalDateTime currentStart, LocalDateTime currentEnd) {
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

        return new AdminGroupCardMetricsResponseDTO(activeCompaniesCard, inactiveCompaniesCard, totalMeetingsCard, activeSellersCard);
    }

    private ManagerGroupCardMetricsResponseDTO buildManagerGroupCard(LocalDateTime previousStart, LocalDateTime previousEnd, LocalDateTime currentStart, LocalDateTime currentEnd, UUID companyId
    ) {
        Long currentActiveSellers = collaboratorRepository.countSellersByCompanyIdAndActiveValue(companyId, true);
        Long previousActiveSellers = collaboratorRepository.countActiveSellersByCompanyIdAndPeriod(companyId, previousEnd);

        Long currentInactiveSellers = collaboratorRepository.countSellersByCompanyIdAndActiveValue(companyId, false);
        Long previousInactiveSellers = collaboratorRepository.countInactiveSellersByCompanyIdAndPeriod(companyId, previousEnd);

        Long currentMeetings = meetingRepository.countTotalMeetingsByCompanyIdAndPeriod(companyId, currentStart, currentEnd);
        Long previousMeetings = meetingRepository.countTotalMeetingsByCompanyIdAndPeriod(companyId, previousStart, previousEnd);

        return new ManagerGroupCardMetricsResponseDTO(
                buildMetricCard(currentActiveSellers, previousActiveSellers),
                buildMetricCard(currentInactiveSellers, previousInactiveSellers),
                buildMetricCard(currentMeetings, previousMeetings)
        );
    }

    private SellerGroupCardMetricsResponseDTO buildSellerGroupCard(LocalDateTime previousStart, LocalDateTime previousEnd, LocalDateTime currentStart, LocalDateTime currentEnd, UUID sellerId
    ) {
        Long currentMeetings = meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(sellerId, currentStart, currentEnd);

        Long previousMeetings = meetingRepository.countTotalMeetingsByCollaboratorIdAndPeriod(sellerId, previousStart, previousEnd);

        Long currentAverageDuration = Math.round(meetingRepository.getAverageDurationByCollaboratorIdAndPeriod(sellerId, currentStart, currentEnd));

        Long previousAverageDuration = Math.round(meetingRepository.getAverageDurationByCollaboratorIdAndPeriod(sellerId, previousStart, previousEnd));

        Double currentAverageSentiment = meetingPostAnalysisRepository.getAverageFeelingByCollaboratorAndPeriod(sellerId, currentStart, currentEnd);

        Double previousAverageSentiment = meetingPostAnalysisRepository.getAverageFeelingByCollaboratorAndPeriod(sellerId, previousStart, previousEnd);

        return new SellerGroupCardMetricsResponseDTO(
                buildMetricCard(currentMeetings, previousMeetings),
                buildMetricCard(currentAverageDuration, previousAverageDuration),
                buildSentimentCard(currentAverageSentiment, previousAverageSentiment)
        );
    }
}