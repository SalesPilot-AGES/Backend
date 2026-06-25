package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.AdminGroupCardMetricsResponseDTO;
import com.salespilot.api.application.dto.AuthUserDTO;
import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.dto.GroupCardMetricsResponse;
import com.salespilot.api.application.dto.GroupStatusCountResponseDTO;
import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.application.usecase.GetAverageMeetingDurationPerMonthUseCase;
import com.salespilot.api.application.usecase.GetCardMetricsUseCase;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.usecase.GetGroupedSellersCountUseCase;
import com.salespilot.api.application.usecase.GetTopFiveCompaniesByMeetingTotalUseCase;
import com.salespilot.api.application.usecase.GetTotalMeetingsGroupedByMonthUseCase;
import com.salespilot.api.presentation.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private final GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase;
    private final GetCardMetricsUseCase getCardMetricsUseCase;
    private final GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase;
    private final GetGroupedSellersCountUseCase getGroupedSellersCountUseCase;

    private static final String COMPANY_STATUS_RESPONSE_EXAMPLE = """
            {
                "data": [
                    { "label": "Ativas", "value": 6 },
                    { "label": "Inativas", "value": 1 }
                ],
                "total": 7
            }
            """;

    public DashboardController(GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth, GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase, GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase, GetCardMetricsUseCase getCardMetricsUseCase, GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase, GetGroupedSellersCountUseCase getGroupedSellersCountUseCase){
        this.getTotalMeetingsGroupedByMonth = getTotalMeetingsGroupedByMonth;
        this.getGroupedCompaniesCountUseCase = getGroupedCompaniesCountUseCase;
        this.getTopFiveCompaniesByMeetingTotalUseCase = getTopFiveCompaniesByMeetingTotalUseCase;
        this.getCardMetricsUseCase = getCardMetricsUseCase;
        this.getAverageMeetingDurationPerMonthUseCase = getAverageMeetingDurationPerMonthUseCase;
        this.getGroupedSellersCountUseCase = getGroupedSellersCountUseCase;
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
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER', 'SELLER')")
    @GetMapping("/meetings-by-month")
    public ResponseEntity<MeetingsGroupedByMonthResponseDTO> getMeetingsGroupedByMonth(
            @AuthenticationPrincipal Jwt jwt,
        @RequestParam(required = true) String period,
        @RequestParam(name = "start_date", required = false) LocalDate startDate,
        @RequestParam(name = "end_date", required = false) LocalDate endDate) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        return ResponseEntity.ok(getTotalMeetingsGroupedByMonth.execute(period, startDate, endDate, authUser));
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
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER')")
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
                            schema = @Schema(implementation = GroupStatusCountResponseDTO.class),
                            examples = @ExampleObject(value = COMPANY_STATUS_RESPONSE_EXAMPLE))),
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("/companies-status")
    public ResponseEntity<GroupStatusCountResponseDTO> getGroupedCompaniesCount() {
        return ResponseEntity.ok(getGroupedCompaniesCountUseCase.execute());
    }

    @Operation(summary = "Buscar as métricas dos cards de dashboard", description = "Retorna as métricas dos cards de dashboard de acordo com o papel do usuário. No contexto de vendedor, inclui total de reuniões, duração média e sentimento médio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas retornadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminGroupCardMetricsResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Período inválido", content = @Content),
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER', 'SELLER')")
    @GetMapping("/metrics")
    public ResponseEntity<GroupCardMetricsResponse> getCardMetrics(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String period,
            @RequestParam(name = "start_date", required = false) LocalDate startDate,
            @RequestParam(name = "end_date", required = false) LocalDate endDate) {

        AuthUserDTO authUser = JwtUtils.toAuthUserDTO(jwt);
        return ResponseEntity.ok(getCardMetricsUseCase.execute(period, startDate, endDate, authUser));
    }

    @Operation(summary = "Retornar a média da duração das reuniões por mês", description = "Calcula e retorna a média da duração das reuniões em todos os meses que tiveram reuniões de acordo com o período requisitado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Média da duração das reuniões por mês retornada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupAverageMeetingDurationPerMonthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Período inválido", content = @Content),
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'MANAGER', 'SELLER')")
    @GetMapping("/avg-duration")
    public ResponseEntity<GroupAverageMeetingDurationPerMonthResponseDTO> getAverageMeetingDurationPerMonth(
        @RequestParam String period,
        @RequestParam(name = "start_date", required = false) LocalDate startDate,
        @RequestParam(name = "end_date", required = false) LocalDate endDate)
    {
        return ResponseEntity.ok(getAverageMeetingDurationPerMonthUseCase.execute(period, startDate, endDate));
    }

    @Operation(summary = "Retornar vendedores ativos e inativos", description = "Retorna a quantidade de vendedores ativos e inativos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupStatusCountResponseDTO.class),
                            examples = @ExampleObject(value = ""))),
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("/sellers-status")
    public ResponseEntity<GroupStatusCountResponseDTO> getGroupedSellersCount() {
        return ResponseEntity.ok(getGroupedSellersCountUseCase.execute());
    }
}