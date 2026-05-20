package com.salespilot.api.presentation.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.TopFiveCompanyByMeetingTotalResponseDto;
import com.salespilot.api.application.usecase.GetTopFiveCompaniesByMeetingTotalUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase;

    public DashboardController(GetTopFiveCompaniesByMeetingTotalUseCase getTopFiveCompaniesByMeetingTotalUseCase){
        this.getTopFiveCompaniesByMeetingTotalUseCase = getTopFiveCompaniesByMeetingTotalUseCase;
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
    public ResponseEntity<TopFiveCompanyByMeetingTotalResponseDto> getMeetingsGroupedByMonth(
        @RequestParam(required = false) String period,
        @RequestParam(required = false) LocalDate start_date,
        @RequestParam(required = false) LocalDate end_date) {
        return ResponseEntity.ok(getTopFiveCompaniesByMeetingTotalUseCase.execute(period, start_date, end_date));
    }
    
}
