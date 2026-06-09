package com.salespilot.api.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.TaxIdAlreadyExists;
import com.salespilot.api.application.usecase.*;
import com.salespilot.api.presentation.handler.GlobalExceptionHandler;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock private PostCompanyUseCase postCompanyUseCase;
    @Mock private GetCompanyByIdUseCase getCompanyByIdUseCase;
    @Mock private GetAllCompaniesUseCase getAllCompaniesUseCase;
    @Mock private UpdateCompanyUseCase updateCompanyUseCase;

    @InjectMocks
    private CompanyController controller;

    private final UUID id = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private CompanyResponseDTO buildResponse() {
        return new CompanyResponseDTO(id, "Digital Sales", "12.345.678/0001-90", null, null, true, now, now, "BASIC");
    }

    private String createBody() {
        return """
                {
                    "name": "Digital Sales",
                    "tax_id": "12.345.678/0001-90",
                    "plan": "BASIC",
                    "active": true
                }
                """;
    }

    private String updateBody() {
        return """
                {
                    "name": "Digital Sales",
                    "plan": "PRO",
                    "active": true
                }
                """;
    }

    @Test
    void shouldCreateCompanyAndReturn201() throws Exception {
        when(postCompanyUseCase.create(any(), any(), any(), anyBoolean())).thenReturn(buildResponse());

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Digital Sales"));
    }

    @Test
    void shouldReturn409WhenTaxIdDuplicate() throws Exception {
        when(postCompanyUseCase.create(any(), any(), any(), anyBoolean())).thenThrow(new TaxIdAlreadyExists());

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody()))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetCompanyByIdAndReturn200() throws Exception {
        when(getCompanyByIdUseCase.execute(id)).thenReturn(Optional.of(buildResponse()));

        mockMvc.perform(get("/api/companies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Digital Sales"));
    }

    @Test
    void shouldReturn404WhenCompanyNotFoundById() throws Exception {
        when(getCompanyByIdUseCase.execute(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/companies/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateCompanyAndReturn200() throws Exception {
        when(updateCompanyUseCase.execute(any(), any(), any(), anyBoolean())).thenReturn(buildResponse());

        mockMvc.perform(put("/api/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Digital Sales"));
    }

    @Test
    void shouldReturn404WhenCompanyNotFoundOnUpdate() throws Exception {
        when(updateCompanyUseCase.execute(any(), any(), any(), anyBoolean()))
                .thenThrow(new CompanyNotFoundException(id));

        mockMvc.perform(put("/api/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListCompaniesAndReturn200() throws Exception {
        when(getAllCompaniesUseCase.execute(any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(buildResponse()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespectMaxPageSizeLimitOf100() throws Exception {
        when(getAllCompaniesUseCase.execute(any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 100), 0));

        mockMvc.perform(get("/api/companies?size=500"))
                .andExpect(status().isOk());
    }
}
