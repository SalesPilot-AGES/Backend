package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.SystemStatusResponseDTO;
import com.salespilot.api.application.usecase.GetSystemStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System")
@RestController
@RequestMapping("/api/v1/system")
public class SystemStatusController {

    private final GetSystemStatusUseCase getSystemStatusUseCase;

    public SystemStatusController(GetSystemStatusUseCase getSystemStatusUseCase) {
        this.getSystemStatusUseCase = getSystemStatusUseCase;
    }

    @Operation(summary = "Health check", description = "Retorna o status atual da API e o horário da verificação.")
    @ApiResponse(responseCode = "200", description = "API operacional",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SystemStatusResponseDTO.class),
                    examples = @ExampleObject(value = """
                            {
                              "current_status": "UP",
                              "checked_at": "2024-04-15T20:00:00"
                            }
                            """)))
    @GetMapping("/ping")
    public ResponseEntity<SystemStatusResponseDTO> ping() {
        return ResponseEntity.ok(getSystemStatusUseCase.execute());
    }
}
