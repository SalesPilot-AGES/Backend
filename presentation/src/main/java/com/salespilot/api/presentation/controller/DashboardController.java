package com.salespilot.api.presentation.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.GroupAverageMeetingDurationPerMonthResponseDTO;
import com.salespilot.api.application.usecase.GetAverageMeetingDurationPerMonthUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase;

    public DashboardController(GetAverageMeetingDurationPerMonthUseCase getAverageMeetingDurationPerMonthUseCase) {
        this.getAverageMeetingDurationPerMonthUseCase = getAverageMeetingDurationPerMonthUseCase;
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
