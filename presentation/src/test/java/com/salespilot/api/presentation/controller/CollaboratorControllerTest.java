package com.salespilot.api.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salespilot.api.application.dto.*;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.usecase.*;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
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

import org.springframework.data.domain.PageRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CollaboratorControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock private PostCollaboratorUseCase postCollaboratorUseCase;
    @Mock private EditCollaboratorUseCase editCollaboratorUseCase;
    @Mock private GetCollaboratorByIdUseCase getCollaboratorByIdUseCase;
    @Mock private GetAllManagersUseCase getAllManagersUseCase;
    @Mock private GetAllSellersUseCase getAllSellersUseCase;
    @Mock private GetSellerByIdUseCase getSellerByIdUseCase;

    @InjectMocks
    private CollaboratorController controller;

    private final UUID id = UUID.randomUUID();
    private final UUID companyId = UUID.randomUUID();
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
        objectMapper = new ObjectMapper()
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

    private CollaboratorResponseDTO buildCollaboratorResponse(CollaboratorRole role) {
        CompanyResponseDTO company = new CompanyResponseDTO(companyId, "Acme", "12.345.678/0001-90", null, null, true, now, now, "BASIC");
        return new CollaboratorResponseDTO(id, companyId, "Ana Silva", role, "ana@acme.com", null, true, null, now, now, company);
    }

    private String collaboratorBody() {
        return """
                {
                    "company_id": "%s",
                    "name": "Ana Silva",
                    "email": "ana@acme.com",
                    "active": true
                }
                """.formatted(companyId);
    }

    @Test
    void shouldCreateManagerAndReturn201() throws Exception {
        setAdminAuth();
        when(postCollaboratorUseCase.create(any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenReturn(buildCollaboratorResponse(CollaboratorRole.MANAGER));

        mockMvc.perform(post("/api/collaborators/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.name").value("Ana Silva"));
    }

    @Test
    void shouldReturn404WhenCompanyNotFoundOnCreateManager() throws Exception {
        setAdminAuth();
        when(postCollaboratorUseCase.create(any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenThrow(new CompanyNotFoundException(companyId));

        mockMvc.perform(post("/api/collaborators/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenEmailDuplicateOnCreateManager() throws Exception {
        setAdminAuth();
        when(postCollaboratorUseCase.create(any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenThrow(new CollaboratorAlreadyExistsException(companyId, "ana@acme.com"));

        mockMvc.perform(post("/api/collaborators/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldEditManagerAndReturn200() throws Exception {
        setAdminAuth();
        when(editCollaboratorUseCase.execute(any(), any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenReturn(buildCollaboratorResponse(CollaboratorRole.MANAGER));

        mockMvc.perform(put("/api/collaborators/managers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.name").value("Ana Silva"));
    }

    @Test
    void shouldReturn404WhenCollaboratorNotFoundOnEdit() throws Exception {
        setAdminAuth();
        when(editCollaboratorUseCase.execute(any(), any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenThrow(new CollaboratorNotFoundException(id));

        mockMvc.perform(put("/api/collaborators/managers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetManagerByIdAndReturn200() throws Exception {
        when(getCollaboratorByIdUseCase.execute(id)).thenReturn(buildCollaboratorResponse(CollaboratorRole.MANAGER));

        mockMvc.perform(get("/api/collaborators/managers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.name").value("Ana Silva"));
    }

    @Test
    void shouldReturn404WhenManagerNotFound() throws Exception {
        when(getCollaboratorByIdUseCase.execute(id)).thenThrow(new CollaboratorNotFoundException(id));

        mockMvc.perform(get("/api/collaborators/managers/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListManagersAndReturn200() throws Exception {
        when(getAllManagersUseCase.execute(any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(buildCollaboratorResponse(CollaboratorRole.MANAGER)), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/collaborators/managers"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateSellerAndReturn201() throws Exception {
        setAdminAuth();
        when(postCollaboratorUseCase.create(any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenReturn(buildCollaboratorResponse(CollaboratorRole.SELLER));

        mockMvc.perform(post("/api/collaborators/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldEditSellerAndReturn200() throws Exception {
        setAdminAuth();
        when(editCollaboratorUseCase.execute(any(), any(), any(), any(), any(), anyBoolean(), any(), any(), any()))
                .thenReturn(buildCollaboratorResponse(CollaboratorRole.SELLER));

        mockMvc.perform(put("/api/collaborators/sellers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(collaboratorBody()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetSellerByIdAndReturn200() throws Exception {
        setAdminAuth();
        CompanyResponseDTO company = new CompanyResponseDTO(companyId, "Acme", "12.345.678/0001-90", null, null, true, now, now, "BASIC");
        SellerWithMeetingsResponseDTO response = new SellerWithMeetingsResponseDTO(
                id, companyId, "Ana Silva", CollaboratorRole.SELLER, "ana@acme.com",
                true, null, null, 5L, null, now, now, company);
        when(getSellerByIdUseCase.execute(any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/collaborators/sellers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.name").value("Ana Silva"));
    }

    @Test
    void shouldListSellersAndReturn200() throws Exception {
        setAdminAuth();
        when(getAllSellersUseCase.execute(any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/api/collaborators/sellers"))
                .andExpect(status().isOk());
    }
}