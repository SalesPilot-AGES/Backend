package com.salespilot.api.presentation.controller;

import com.salespilot.api.application.dto.CollaboratorResponseDTO;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;
import com.salespilot.api.application.usecase.GetAllManagersUseCase;
import com.salespilot.api.application.usecase.GetCollaboratorByIdUseCase;
import com.salespilot.api.application.usecase.PostCollaboratorUseCase;
import com.salespilot.api.domain.enums.CollaboratorRole;
import com.salespilot.api.presentation.dto.CollaboratorRequestDTO;
import com.salespilot.api.presentation.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {

    private final PostCollaboratorUseCase postCollaboratorUseCase;
    private final EditCollaboratorUseCase editCollaboratorUseCase;
    private final GetCollaboratorByIdUseCase getCollaboratorByIdUseCase;
    private final GetAllManagersUseCase getAllManagersUseCase;

    public CollaboratorController(PostCollaboratorUseCase postCollaboratorUseCase,
                                  EditCollaboratorUseCase editCollaboratorUseCase,
                                  GetCollaboratorByIdUseCase getCollaboratorByIdUseCase,
                                  GetAllManagersUseCase getAllManagersUseCase) {
        this.postCollaboratorUseCase = postCollaboratorUseCase;
        this.editCollaboratorUseCase = editCollaboratorUseCase;
        this.getCollaboratorByIdUseCase = getCollaboratorByIdUseCase;
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

    @PutMapping("/managers/{id}")
    public ResponseEntity<CollaboratorResponseDTO> editCollaborator(@PathVariable UUID id, @Valid @RequestBody CollaboratorRequestDTO request) {
        CollaboratorResponseDTO response = editCollaboratorUseCase.execute(
            request.companyId(),
            id,
            request.name(),
            request.email(),
            request.active(),
            request.preferences());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/managers/{id}")
    public ResponseEntity<CollaboratorResponseDTO> getManagerById(@PathVariable UUID id) {
        CollaboratorResponseDTO collaboratorResponseDTO = getCollaboratorByIdUseCase.execute(id);

        return ResponseEntity.ok(collaboratorResponseDTO);
    }

    @GetMapping("/managers")
    public ResponseEntity<Page<CollaboratorResponseDTO>> getManagers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        return ResponseEntity.ok(getAllManagersUseCase.execute(name, email, companyId, active, PageableUtils.normalize(pageable)));
    }
}
