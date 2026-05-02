package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.dto.MeetingPageResponseDTO;
import com.salespilot.api.application.dto.MeetingPostAnalysisResponseDTO;
import com.salespilot.api.application.usecase.GetAllMeetingsUseCase;
import com.salespilot.api.application.usecase.GetMeetingContextAndMetadataUseCase;
import com.salespilot.api.application.usecase.GetMeetingPostAnalysisUseCase;
import com.salespilot.api.presentation.utils.PageableUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Meetings")
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final GetAllMeetingsUseCase getAllMeetingsUseCase;
    private final GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase;
    private final GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase;

    public MeetingController(GetAllMeetingsUseCase getAllMeetingsUseCase, GetMeetingContextAndMetadataUseCase getMeetingContextAndMetadataUseCase, GetMeetingPostAnalysisUseCase getMeetingPostAnalysisUseCase) {
        this.getAllMeetingsUseCase = getAllMeetingsUseCase;
        this.getMeetingContextAndMetadataUseCase = getMeetingContextAndMetadataUseCase;
        this.getMeetingPostAnalysisUseCase = getMeetingPostAnalysisUseCase;
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

    @GetMapping("/{id}/action-plan")
    public ResponseEntity<MeetingPostAnalysisResponseDTO> getMeetingPostAnalysis(@PathVariable UUID id) {
        return ResponseEntity.ok(getMeetingPostAnalysisUseCase.execute(id));
    }
}
