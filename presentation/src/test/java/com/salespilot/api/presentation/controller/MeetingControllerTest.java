package com.salespilot.api.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salespilot.api.application.dto.*;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.application.exception.MeetingPostAnalysisNotFoundException;
import com.salespilot.api.application.usecase.*;
import com.salespilot.api.domain.enums.RealtimeInsightType;
import com.salespilot.api.domain.valueobject.InsightDescription;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MeetingControllerTest {

    private MockMvc mockMvc;

    @Mock private GetAllMeetingsUseCase getAllMeetingsUseCase;
    @Mock private GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase;
    @Mock private GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase;
    @Mock private GetMeetingInsightUseCase getMeetingInsightUseCase;

    @InjectMocks
    private MeetingController controller;

    private final UUID meetingId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

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
    void shouldListMeetingsAndReturn200() throws Exception {
        setAdminAuth();
        MeetingPageResponseDTO response = new MeetingPageResponseDTO(
                List.of(), 0L, 0, new SummaryResponseDTO(0L, 0.0, null));
        when(getAllMeetingsUseCase.execute(any(), any(), any(), any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/meetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.totalElements").value(0));
    }

    @Test
    void shouldGetMeetingByIdAndReturn200() throws Exception {
        setAdminAuth();
        SellerMeetingResponseDTO seller = new SellerMeetingResponseDTO(UUID.randomUUID(), "Ana", "ana@acme.com");
        ClientMeetingResponseDTO client = new ClientMeetingResponseDTO(UUID.randomUUID(), "Carlos", "Tech", "TI", 80);
        MeetingContextMetadataResponseDTO response = new MeetingContextMetadataResponseDTO(
                meetingId, "Reunião de descoberta", "SCHEDULED", "ONLINE",
                "Objetivo", "Necessidades", null, null,
                now, null, null, 1800, seller, client, null, now);
        when(getMeetingContextAndMetadataUseCase.execute(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/meetings/{id}", meetingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.title").value("Reunião de descoberta"));
    }

    @Test
    void shouldReturn404WhenMeetingNotFound() throws Exception {
        setAdminAuth();
        when(getMeetingContextAndMetadataUseCase.execute(any(), any()))
                .thenThrow(new MeetingNotFoundException(meetingId));

        mockMvc.perform(get("/api/meetings/{id}", meetingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetPostAnalysisAndReturn200() throws Exception {
        setAdminAuth();
        MeetingPostAnalysisResponseDTO response = new MeetingPostAnalysisResponseDTO(
                UUID.randomUUID(), meetingId, "Resumo da reunião", List.of(), null, now);
        when(getMeetingPostAnalysisUseCase.execute(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/meetings/{id}/post-analysis", meetingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.summary").value("Resumo da reunião"));
    }

    @Test
    void shouldReturn404WhenPostAnalysisNotFound() throws Exception {
        setAdminAuth();
        when(getMeetingPostAnalysisUseCase.execute(any(), any()))
                .thenThrow(new MeetingPostAnalysisNotFoundException(meetingId));

        mockMvc.perform(get("/api/meetings/{id}/post-analysis", meetingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetInsightsAndReturn200() throws Exception {
        setAdminAuth();
        List<MeetingRealtimeInsightsResponseDTO> insights = List.of(
                new MeetingRealtimeInsightsResponseDTO(
                        UUID.randomUUID(), RealtimeInsightType.KEY_POINT,
                        new InsightDescription("Urgência de integração"), "Conteúdo", now));
        when(getMeetingInsightUseCase.execute(any(), any())).thenReturn(insights);

        mockMvc.perform(get("/api/meetings/{id}/insights", meetingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].type").value("KEY_POINT"));
    }

    @Test
    void shouldReturn404WhenMeetingNotFoundForInsights() throws Exception {
        setAdminAuth();
        when(getMeetingInsightUseCase.execute(any(), any()))
                .thenThrow(new MeetingNotFoundException(meetingId));

        mockMvc.perform(get("/api/meetings/{id}/insights", meetingId))
                .andExpect(status().isNotFound());
    }
}