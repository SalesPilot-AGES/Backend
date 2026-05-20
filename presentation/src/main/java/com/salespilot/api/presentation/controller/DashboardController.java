package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.MeetingsGroupedByMonthResponseDTO;
import com.salespilot.api.application.usecase.GetTotalMeetingsGroupedByMonthUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

    public DashboardController(GetTotalMeetingsGroupedByMonthUseCase getTotalMeetingsGroupedByMonth){
        this.getTotalMeetingsGroupedByMonth = getTotalMeetingsGroupedByMonth;
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
    
}