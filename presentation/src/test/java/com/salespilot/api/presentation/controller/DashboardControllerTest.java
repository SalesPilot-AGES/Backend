package com.salespilot.api.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salespilot.api.application.dto.AdminGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.CardMetricsResponseDTO;
import com.salespilot.api.application.dto.CompanyNameAndTotalMeetingsDto;
import com.salespilot.api.application.dto.DashboardStatusCountDTO;
import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.dto.GroupStatusCountResponseDTO;
import com.salespilot.api.application.dto.MeetingsBySellerResponseDto;
import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.dto.SellerNameAndTotalMeetingsDto;
import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.application.exception.InvalidPeriodException;
import com.salespilot.api.application.usecase.GetAverageMeetingDurationPerMonthUseCase;
import com.salespilot.api.application.usecase.GetCardMetricsUseCase;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.usecase.GetGroupedSellersCountUseCase;
import com.salespilot.api.application.usecase.GetMeetingsBySellerUseCase;
import com.salespilot.api.application.usecase.GetTopFiveCompaniesByMeetingTotalUseCase;
import com.salespilot.api.application.usecase.GetTotalMeetingsGroupedByMonthUseCase;
import com.salespilot.api.presentation.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    private MockMvc mockMvc;

    @Mock GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth;
    @Mock GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase;
    @Mock GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase;
    @Mock GetCardMetricsUseCase getCardMetricsUseCase;
    @Mock GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase;
    @Mock
    GetGroupedSellersCountUseCase getGroupedSellersCountUseCase;
    @Mock
    GetMeetingsBySellerUseCase getMeetingsBySellerUseCase;

    @InjectMocks
    DashboardController controller;

    private void setAdminAuth() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("role", "SYSTEM_ADMIN")
                .claim("sub", UUID.randomUUID().toString())
                .claim("company_id", UUID.randomUUID().toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(jwt, Collections.emptyList()));
    }

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver(),
                        new AuthenticationPrincipalArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetMeetingsGroupedByMonthAndReturn200() throws Exception {
        setAdminAuth();
        when(getTotalMeetingsGroupedByMonth.execute(any(), any(), any(), any()))
                .thenReturn(new MeetingsGroupedByMonthResponseDTO(List.of()));

        mockMvc.perform(get("/api/dashboard/meetings-by-month?period=30d"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldGetTopFiveCompaniesByMeetingTotalAndReturn200() throws Exception {
        when(getTopFiveCompaniesByMeetingTotalUseCase.execute(any(), any(), any()))
                .thenReturn(new TopFiveCompanyByMeetingTotalResponseDto(
                        List.of(new CompanyNameAndTotalMeetingsDto("Acme", 10L))));

        mockMvc.perform(get("/api/dashboard/meetings-by-company?period=all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].company_name").value("Acme"));
    }

    @Test
    void shouldGetMeetingsBySellerAndReturn200() throws Exception {
        when(getMeetingsBySellerUseCase.execute(any(), any(), any()))
                .thenReturn(new MeetingsBySellerResponseDto(
                        List.of(new SellerNameAndTotalMeetingsDto("Ana Silva", 18L))));

        mockMvc.perform(get("/api/dashboard/meetings-by-seller?period=all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].seller_name").value("Ana Silva"))
                .andExpect(jsonPath("$.data[0].total").value(18));
    }

    @Test
    void shouldReturn400WhenInvalidPeriodOnMeetingsBySeller() throws Exception {
        when(getMeetingsBySellerUseCase.execute(any(), any(), any()))
                .thenThrow(new InvalidPeriodException(null, null));

        mockMvc.perform(get("/api/dashboard/meetings-by-seller?period=invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCompaniesStatusAndReturn200() throws Exception {
        when(getGroupedCompaniesCountUseCase.execute())
                .thenReturn(new GroupStatusCountResponseDTO(
                        List.of(new DashboardStatusCountDTO("Ativas", 5L)), 5L));

        mockMvc.perform(get("/api/dashboard/companies-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5));
    }

    @Test
    void shouldGetCardMetricsAndReturn200() throws Exception {
        setAdminAuth();
        when(getCardMetricsUseCase.execute(any(), any(), any(), any()))
                .thenReturn(new AdminGroupCardMetricsResponseDTO(
                        new CardMetricsResponseDTO(10L, 0.0, "neutral"),
                        new CardMetricsResponseDTO(2L, 0.0, "neutral"),
                        new CardMetricsResponseDTO(50L, 5.0, "up"),
                        new CardMetricsResponseDTO(8L, 0.0, "neutral")));

        mockMvc.perform(get("/api/dashboard/metrics?period=30d"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAverageDurationPerMonthAndReturn200() throws Exception {
        when(getAverageMeetingDurationPerMonthUseCase.execute(any(), any(), any()))
                .thenReturn(new GroupAverageMeetingDurationPerMonthResponseDTO(List.of()));

        mockMvc.perform(get("/api/dashboard/avg-duration?period=30d"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldReturn400WhenInvalidPeriodOnMetrics() throws Exception {
        setAdminAuth();
        when(getCardMetricsUseCase.execute(any(), any(), any(), any()))
                .thenThrow(new InvalidPeriodException(null, null));

        mockMvc.perform(get("/api/dashboard/metrics?period=invalid"))
                .andExpect(status().isBadRequest());
    }
}
