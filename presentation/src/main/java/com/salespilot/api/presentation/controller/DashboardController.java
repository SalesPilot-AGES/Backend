package com.salespilot.api.presentation.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.salespilot.api.application.dto.GroupCompanyCountResponseDTO;
import com.salespilot.api.application.usecase.GetGroupedCompaniesCountUseCase;
import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.usecase.GetAverageMeetingDurationPerMonthUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase;
    private final GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase;

    private static final String COMPANY_STATUS_RESPONSE_EXAMPLE = """
            {
                "data": [
                    { "label": "Ativas", "value": 6 },
                    { "label": "Inativas", "value": 1 }
                ],
                "total": 7
            }
            """;

    public DashboardController(GetGroupedCompaniesCountUseCase getGroupedCompaniesCountUseCase, GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase){
        this.getGroupedCompaniesCountUseCase = getGroupedCompaniesCountUseCase;
        this.getAverageMeetingDurationPerMonthUseCase = getAverageMeetingDurationPerMonthUseCase;
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

    @Operation(summary = "Retornar a média da duração das reuniões por mês", description = "Calcula e retorna a média da duração das reuniões em todos os meses que tiveram reuniões de acordo com o período requisitado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Média da duração das reuniões por mês retornada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupAverageMeetingDurationPerMonthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Período inválido", content = @Content),
    })
    @GetMapping("/avg-duration")
    public ResponseEntity<GroupAverageMeetingDurationPerMonthResponseDTO> getAverageMeetingDurationPerMonth(
        @RequestParam String period,
        @RequestParam(name = "start_date", required = false) LocalDate startDate,
        @RequestParam(name = "end_date", required = false) LocalDate endDate)
    {
        return ResponseEntity.ok(getAverageMeetingDurationPerMonthUseCase.execute(period, startDate, endDate));
    }
}