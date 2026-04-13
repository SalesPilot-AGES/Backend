package com.salespilot.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.EditCollaboratorDTO;
import com.salespilot.api.application.usecase.EditCollaboratorUseCase;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/gestores")
public class CollaboratorController {
    private final EditCollaboratorUseCase editCollaboratorUseCase;

    public CollaboratorController(EditCollaboratorUseCase editCollaboratorUseCase){
        this.editCollaboratorUseCase = editCollaboratorUseCase;
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditCollaboratorDTO> editCollaborator(@PathVariable UUID id, @RequestBody EditCollaboratorDTO editCollaboratorDTO) {
        //return editCollaboratorUseCase.execute(id, editCollaboratorDTO.companyId(), editCollaboratorDTO.name(), editCollaboratorDTO.role(), editCollaboratorDTO.email(), editCollaboratorDTO.active(), editCollaboratorDTO.preferences(),null , null); 
        return null;
    }
}
