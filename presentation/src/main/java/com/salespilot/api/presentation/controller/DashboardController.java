package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.usecase.GetTopFiveCompaniesByMeetingTotalUseCase;
import com.salespilot.api.application.usecase.GetTotalMeetingsGroupedByMonthUseCase;
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
    private final GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth;
    private final GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase;
    private final GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase;
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

    public DashboardController(GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase, GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth, GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase, GetCardMetricsUseCase getCardMetricsUseCase){
        this.getGroupedCompaniesCountUseCase = getGroupedCompaniesCountUseCase;
        this.getTotalMeetingsGroupedByMonth = getTotalMeetingsGroupedByMonth;
        this.getTopFiveCompaniesByMeetingTotalUseCase = getTopFiveCompaniesByMeetingTotalUseCase;
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
    @GetMapping("/meetings-by-month")
    public ResponseEntity<MeetingsGroupedByMonthResponseDTO> getMeetingsGroupedByMonth(
        @RequestParam(required = true) String period,
        @RequestParam(name = "start_date", required = false) LocalDate startDate,
        @RequestParam(name = "end_date", required = false) LocalDate endDate) {
        return ResponseEntity.ok(getTotalMeetingsGroupedByMonth.execute(period, startDate, endDate));
    }

    @Operation(summary = "Listar top 5 empresas por número de reuniões", description = "Retorna uma lista contendo o nome da empresa e a quantidade de reuniões, limitado a 5 empresas em ordem decrescente de reuniões, de todo período ou intervalo de datas personalizadas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "data": [
                                    { "company_name": "Tech Solutions Ltda", "total": 25 },
                                    { "company_name": "Digital Sales", "total": 18 },
                                    { "company_name": "Enterprise Tech Brasil", "total": 12 },
                                    { "company_name": "InovaCorp", "total": 9 },
                                    { "company_name": "Smart Vendas", "total": 6 }
                                ]
                            }
                            """))),
        @ApiResponse(responseCode = "400", description = "Parâmetros 'start_date' ou 'end_date' inválidos")
    })
    @GetMapping("/meetings-by-company")
    public ResponseEntity<TopFiveCompanyByMeetingTotalResponseDto> getTopFiveCompaniesByMeetingTotal(
        @RequestParam(required = true) String period,
        @RequestParam(name = "start_date", required = false) LocalDate startDate,
        @RequestParam(name = "end_date", required = false) LocalDate endDate) {
        return ResponseEntity.ok(getTopFiveCompaniesByMeetingTotalUseCase.execute(period, startDate, endDate));
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