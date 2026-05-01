package com.salespilot.api.presentation.controller;

import com.salespilot.api.presentation.utils.PageableUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Meetings")
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final GetAllMeetingsUseCase getAllMeetingsUseCase;


    public MeetingController(GetAllMeetingsUseCase getAllMeetingsUseCase) {
        this.getAllMeetingsUseCase = getAllMeetingsUseCase;
    }

    @GetMapping
    public ResponseEntity<MeetingPageResponseDTO> getAllMeetings(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String clientCompanyName,
            @RequestParam(required = false) UUID collaboratorId,
            Pageable pageable) {

        return ResponseEntity.ok(getAllMeetingsUseCase.execute(title, clientCompanyName, collaboratorId, PageableUtils.normalize(pageable)));
    }
}
