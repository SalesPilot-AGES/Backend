package com.salespilot.api.presentation.controller;

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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @GetMapping
    public ResponseEntity<MeetingPageResponseDTO> getAllMeetings(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String clientCompanyName,
            @RequestParam(required = false) UUID collaboratorId,
            Pageable pageable) {

        return ResponseEntity.ok(getAllMeetingsUseCase.execute(title, clientCompanyName, collaboratorId, PageableUtils.normalize(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingContextMetadataResponseDTO> getMeetingContextAndMetadata(@PathVariable UUID id) {
        return ResponseEntity.ok(getMeetingContextAndMetadataUseCase.execute(id));
    }

    @Operation(summary = "Buscar pós-análise da reunião", description = "Retorna o pós-análise de uma reunião com resumo, itens de ação e análise de sentimento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pós-análise encontrada",
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
            @ApiResponse(responseCode = "404", description = "Pós-análise da reunião não encontrada", content = @Content)
    })
    @GetMapping("/{id}/post-analysis")
    public ResponseEntity<MeetingPostAnalysisResponseDTO> getMeetingPostAnalysis(
            @Parameter(description = "UUID da reunião") @PathVariable UUID id) {
        return ResponseEntity.ok(getMeetingPostAnalysisUseCase.execute(id));
    }



    @Operation(summary = "Buscar insights da reunião", description = "Retorna os insights gerados automaticamente para uma reunião.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Insights encontrados",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingRealtimeInsightsResponseDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "content": [
                        {
                          "id": "i1a2b3c4-d5e6-7890-1234-56789abcdef0",
                          "type": "KEY_POINT",
                          "description": "Cliente destacou urgência de integração",
                          "content": "A equipe precisa conectar CRM e ERP ainda neste trimestre.",
                          "created_at": "2024-06-10T14:15:00Z"
                        },
                        {
                          "id": "i2b3c4d5-e6f7-8901-2345-6789abcdef01",
                          "type": "ACTION_ITEM",
                          "description": "Enviar proposta atualizada",
                          "content": "Revisar valores e enviar nova proposta até sexta-feira.",
                          "created_at": "2024-06-10T14:20:00Z"
                        }
                      ]
                    }
                """))),
        @ApiResponse(responseCode = "404", description = "Reunião não encontrada", content = @Content)
    })
    @GetMapping("/{id}/insights")
    public ResponseEntity<List<MeetingRealtimeInsightsResponseDTO>> getMeetingRealtimeInsights(
            @PathVariable UUID id, Pageable pageable){
        return ResponseEntity.ok(getMeetingInsightUseCase.execute(id));
    }
}
