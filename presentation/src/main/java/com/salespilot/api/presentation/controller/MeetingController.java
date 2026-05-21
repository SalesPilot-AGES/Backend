package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.ApiResponse;
import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.dto.MeetingPageResponseDTO;
import com.salespilot.api.application.dto.MeetingPostAnalysisResponseDTO;
import com.salespilot.api.application.dto.MeetingRealtimeInsightsResponseDTO;
import com.salespilot.api.application.usecase.GetAllMeetingsUseCase;
import com.salespilot.api.application.usecase.GetMeetingContextAndMetadataUseCase;
import com.salespilot.api.application.usecase.GetMeetingInsightUseCase;
import com.salespilot.api.application.usecase.GetMeetingPostAnalysisUseCase;
import com.salespilot.api.presentation.utils.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Meetings")
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final GetAllMeetingsUseCase getAllMeetingsUseCase;
    private final GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase;
    private final GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase;
    private final GetMeetingInsightUseCase getMeetingInsightUseCase;

    public MeetingController(GetAllMeetingsUseCase getAllMeetingsUseCase, GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase, GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase, GetMeetingInsightUseCase getMeetingInsightUseCase) {
        this.getAllMeetingsUseCase = getAllMeetingsUseCase;
        this.getMeetingContextAndMetadataUseCase = getMeetingContextAndMetadataUseCase;
        this.getMeetingPostAnalysisUseCase = getMeetingPostAnalysisUseCase;
        this.getMeetingInsightUseCase = getMeetingInsightUseCase;
    }

    @Operation(summary = "Listar reuniões", description = "Retorna uma página de reuniões com filtros opcionais e métricas gerais.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reuniões retornadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeetingPageResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "content": [
                                            {
                                                "id": "99999999-8888-7777-6666-555555555555",
                                                "title": "Reuniao de descoberta",
                                                "seller": {
                                                    "id": "e5f6a7b8-c9d0-1234-5678-90abcdef1234",
                                                    "name": "Saulo Souza",
                                                    "email": "saulo@digitalsales.com"
                                                },
                                                "client": {
                                                    "id": "11111111-2222-3333-4444-555555555555",
                                                    "name": "Marina Lima",
                                                    "client_company_name": "Alfa Industrial",
                                                    "sector": "Manufacturing",
                                                    "overall_sentiment": 8
                                                },
                                                "meeting_type": "ONLINE",
                                                "scheduled_for": "2026-05-02T15:50:24.533071",
                                                "started_at": null,
                                                "ended_at": null,
                                                "duration_seconds": 1800,
                                                "status": "SCHEDULED"
                                            }
                                        ],
                                        "total_elements": 1,
                                        "total_pages": 1,
                                        "summary": {
                                            "total_meetings": 1,
                                            "average_duration_seconds": 1800.0,
                                            "success_rate": 0.0
                                        }
                                    }
                                    """)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<MeetingPageResponseDTO>> getAllMeetings(
            @Parameter(description = "Filtrar por título (parcial)") @RequestParam(required = false) String title,
            @Parameter(description = "Filtrar por empresa do cliente (parcial)") @RequestParam(required = false) String clientCompanyName,
            @Parameter(description = "Filtrar por ID do vendedor") @RequestParam(required = false) UUID collaboratorId,
            Pageable pageable) {
        MeetingPageResponseDTO meetings = getAllMeetingsUseCase.execute(title, clientCompanyName, collaboratorId, PageableUtils.normalize(pageable));
        return ResponseEntity.ok(ApiResponse.success(meetings, "Reuniões listadas com sucesso"));
    }

    @Operation(summary = "Buscar reunião por ID", description = "Retorna os dados completos de contexto e metadados de uma reunião.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reunião encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeetingContextMetadataResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "m1a2b3c4-d5e6-7890-1234-56789abcdef0",
                                      "title": "Reuniao de prospeccao",
                                      "status": "COMPLETED",
                                      "meeting_type": "ONLINE",
                                      "objective": "Apresentar proposta comercial",
                                      "client_needs": "Integracao com CRM e previsibilidade de pipeline",
                                      "previous_interactions": "Demo realizada em 2024-05-20",
                                      "competitors_involved": "Concorrente X",
                                      "scheduled_for": "2024-06-10T14:00:00Z",
                                      "started_at": "2024-06-10T14:03:00Z",
                                      "ended_at": "2024-06-10T14:45:00Z",
                                      "duration_seconds": 2520,
                                      "seller": {
                                        "id": "b2c3d4e5-f6a7-8901-2345-67890abcdef1",
                                        "name": "Ana Silva Vendedora",
                                        "email": "ana.silva@tech.com.br"
                                      },
                                      "client": {
                                        "id": "c1a2b3c4-d5e6-7890-1234-56789abcdef0",
                                        "name": "Carlos Lima",
                                        "client_company_name": "Tech Solutions Ltda",
                                        "sector": "Tecnologia",
                                        "overall_sentiment": 79
                                      },
                                      "pre_analysis": {
                                        "id": "p1a2b3c4-d5e6-7890-1234-56789abcdef0",
                                        "recommended_strategy": {
                                          "focus": "ROI e tempo de implementacao"
                                        },
                                        "key_points": [
                                          "Mapear processo atual",
                                          "Explorar gargalos comerciais"
                                        ],
                                        "possible_objections": [
                                          "Preco",
                                          "Prazo de rollout"
                                        ],
                                        "created_at": "2024-06-10T13:40:00Z"
                                      },
                                      "created_at": "2024-06-10T13:30:00Z"
                                    }
                                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reunião, seller ou cliente não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MeetingContextMetadataResponseDTO>> getMeetingContextAndMetadata(
            @Parameter(description = "UUID da reunião") @PathVariable UUID id) {
        MeetingContextMetadataResponseDTO meeting = getMeetingContextAndMetadataUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(meeting, "Reunião encontrada"));
    }

    @Operation(summary = "Buscar pós-análise da reunião", description = "Retorna o pós-análise de uma reunião com resumo, itens de ação e análise de sentimento.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pós-análise encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeetingPostAnalysisResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "a1a2b3c4-d5e6-7890-1234-56789abcdef0",
                                      "meeting_id": "m1a2b3c4-d5e6-7890-1234-56789abcdef0",
                                      "summary": "Cliente demonstrou interesse na proposta e pediu retorno em 7 dias.",
                                      "action_items": [
                                        {
                                          "text": "Enviar proposta revisada",
                                          "done": false
                                        },
                                        {
                                          "text": "Agendar próxima etapa",
                                          "done": false
                                        }
                                      ],
                                      "sentiment_analysis": {
                                        "overall": "positive",
                                        "score": 0.81
                                      },
                                      "created_at": "2024-06-10T16:00:00Z"
                                    }
                                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pós-análise da reunião não encontrada", content = @Content)
    })

    @GetMapping("/{id}/post-analysis")
    public ResponseEntity<ApiResponse<MeetingPostAnalysisResponseDTO>> getMeetingPostAnalysis(
            @Parameter(description = "UUID da reunião") @PathVariable UUID id) {
        MeetingPostAnalysisResponseDTO postAnalysis = getMeetingPostAnalysisUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(postAnalysis, "Pós-análise da reunião encontrada"));
    }



    @Operation(summary = "Buscar insights da reunião", description = "Retorna os insights gerados automaticamente para uma reunião.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Insights encontrados",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingRealtimeInsightsResponseDTO.class),
                examples = @ExampleObject(value = """
                    [
                      {
                        "id": "i1a2b3c4-d5e6-7890-1234-56789abcdef0",
                        "type": "KEY_POINT",
                        "description": {
                            "text": "Urgência de integração destacada pelo cliente"
                        },
                        "content": "A equipe precisa conectar CRM e ERP ainda neste trimestre.",
                        "created_at": "2024-06-10T14:15:00Z"
                      },
                      {
                        "id": "i2b3c4d5-e6f7-8901-2345-6789abcdef01",
                        "type": "ACTION_ITEM",
                        "description": {
                            "text": "Revisar valores e entregar proposta até sexta-feira"
                        },
                        "content": "Revisar valores e enviar nova proposta até sexta-feira.",
                        "created_at": "2024-06-10T14:20:00Z"
                      }
                    ]
                """))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reunião não encontrada", content = @Content)
    })
    @GetMapping("/{id}/insights")
    public ResponseEntity<ApiResponse<List<MeetingRealtimeInsightsResponseDTO>>> getMeetingRealtimeInsights(
            @Parameter(description = "UUID da reunião") @PathVariable UUID id){
        List<MeetingRealtimeInsightsResponseDTO> insights = getMeetingInsightUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(insights, "Insights da reunião encontrados"));
    }
}
