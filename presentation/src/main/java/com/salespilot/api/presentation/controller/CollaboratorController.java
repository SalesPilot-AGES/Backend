package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {

    private static final int MAX_PAGE_SIZE = 100;

    private final PostCollaboratorUseCase postCollaboratorUseCase;
    private final GetAllManagersUseCase getAllManagersUseCase;

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase, GetAllManagersUseCase getAllManagersUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
        this.getAllManagersUseCase = getAllManagersUseCase;
    }

    @PostMapping("/managers")
    public ResponseEntity<CollaboratorResponseDTO> createManager(@Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = postCollaboratorUseCase.create(
                request.companyId(),
                request.name(),
                request.email(),
                CollaboratorRole.MANAGER,
                request.active(),
                request.preferences()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/managers")
    public ResponseEntity<Page<CollaboratorResponseDTO>> getManagers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Pageable safePageable = normalizePageable(pageable);
        return ResponseEntity.ok(getAllManagersUseCase.execute(name, email, companyId, active, safePageable));
    }

    private Pageable normalizePageable(Pageable pageable) {
        int safePage = Math.max(pageable.getPageNumber(), 0);
        int safeSize = Math.min(Math.max(pageable.getPageSize(), 1), MAX_PAGE_SIZE);
        Sort safeSort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.unsorted();
        return PageRequest.of(safePage, safeSize, safeSort);
    }
}
