package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.dto.GroupCardMetricsResponseDTO;
import com.salespilot.api.application.usecase.GetCardMetricsUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase;
    private final GetCardMetricsUseCase getCardMetricsUseCase;

    private static final String COMPANY_STATUS_RESPONSE_EXAMPLE = """
            {
                "data": [
                    { "label": "Ativas", "value": 6 },
                    { "label": "Inativas", "value": 1 }
                ],
                "total": 7
            }
            """;

    public DashboardController(GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase, GetCardMetricsUseCase getCardMetricsUseCase){
        this.getGroupedCompaniesCountUseCase = getGroupedCompaniesCountUseCase;
        this.getCardMetricsUseCase = getCardMetricsUseCase;
    }


    @Operation(summary = "Retornar empresas ativas e inativas", description = "Retorna a quantidade de empresas ativas e inativas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupCompanyCountResponseDTO.class),
                            examples = @ExampleObject(value = COMPANY_STATUS_RESPONSE_EXAMPLE))),
    })
    @GetMapping("/companies-status")
    public ResponseEntity<GroupCompanyCountResponseDTO> getGroupedCompaniesCount() {
        return ResponseEntity.ok(getGroupedCompaniesCountUseCase.execute());
    }

    @Operation(summary = "Buscar as métricas dos cards de dashboard", description = "Retorna as métricas dos cards de dashboard.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas retornadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupCardMetricsResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Período inválido", content = @Content),
    })
    @GetMapping("/metrics")
    public ResponseEntity<GroupCardMetricsResponseDTO> getCardMetrics(
            @RequestParam String period,
            @RequestParam(name = "start_date", required = false) LocalDate startDate,
            @RequestParam(name = "end_date", required = false) LocalDate endDate) {
        return ResponseEntity.ok(getCardMetricsUseCase.execute(period, startDate, endDate));
    }
    
}