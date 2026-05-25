package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.GroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.usecase.GetCardMetricsUseCase;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.usecase.GetTotalMeetingsGroupedByMonthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth;
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

    public DashboardController(GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth, GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase, GetCardMetricsUseCase getCardMetricsUseCase){
        this.getTotalMeetingsGroupedByMonth = getTotalMeetingsGroupedByMonth;
        this.getGroupedCompaniesCountUseCase = getGroupedCompaniesCountUseCase;
        this.getCardMetricsUseCase = getCardMetricsUseCase;
    }

    @Operation(summary = "Listar reunoões por mês", description = "Retorna uma lista contendo o mês e sua quantidade de reniões a partir de filtros personalizados, 30d, ou 90d.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "data": [
                                    { "month": "2024-05-01T00:00:00Z", "month_label": "Mai", "total": 15 },
                                    { "month": "2024-06-01T00:00:00Z", "month_label": "Jun", "total": 23 }
                                ]
                            }
                            """))),
        @ApiResponse(responseCode = "400", description = "Parâmetros 'start_date' ou 'end_date' inválidos")
    })
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'MANAGER', 'SELLER')")
    @GetMapping("/meetings-by-month")
    public ResponseEntity<MeetingsGroupedByMonthResponseDTO> getMeetingsGroupedByMonth(
        @RequestParam(required = true) String period,
        @RequestParam(name = "start_date", required = false) LocalDate startDate,
        @RequestParam(name = "end_date", required = false) LocalDate endDate) {
        return ResponseEntity.ok(getTotalMeetingsGroupedByMonth.execute(period, startDate, endDate));
    }

    @Operation(summary = "Retornar empresas ativas e inativas", description = "Retorna a quantidade de empresas ativas e inativas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupCompanyCountResponseDTO.class),
                            examples = @ExampleObject(value = COMPANY_STATUS_RESPONSE_EXAMPLE))),
    })
    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @GetMapping("/companies-status")
    public ResponseEntity<GroupCompanyCountResponseDTO> getGroupedCompaniesCount() {
        return ResponseEntity.ok(getGroupedCompaniesCountUseCase.execute());
    }

    @Operation(summary = "Buscar as métricas dos cards de dashboard", description = "Retorna as métricas dos cards de dashboard.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas retornadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupCardMetricsResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Período inválido", content = @Content),
    })
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'MANAGER', 'SELLER')")
    @GetMapping("/metrics")
    public ResponseEntity<GroupCardMetricsResponseDTO> getCardMetrics(
            @RequestParam String period,
            @RequestParam(name = "start_date", required = false) LocalDate startDate,
            @RequestParam(name = "end_date", required = false) LocalDate endDate) {
        return ResponseEntity.ok(getCardMetricsUseCase.execute(period, startDate, endDate));
    }
    
}