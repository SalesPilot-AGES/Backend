package com.salespilot.api.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para definir a senha de um colaborador")
public record CollaboratorPasswordRequestDTO(
        @Schema(description = "Senha do colaborador", example = "ChangeMe123!")
        @NotBlank
        @Size(min = 8, max = 72) String password
) {}

