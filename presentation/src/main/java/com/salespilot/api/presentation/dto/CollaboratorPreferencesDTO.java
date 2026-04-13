package com.salespilot.api.presentation.dto;

import com.salespilot.api.domain.valueobject.CollaboratorPreferences;
import jakarta.validation.constraints.NotBlank;

public record CollaboratorPreferencesDTO(
        @NotBlank
        String theme,

        @NotBlank
        String defaultModel
) {
    public static CollaboratorPreferencesDTO from(CollaboratorPreferences preferences) {
        return new CollaboratorPreferencesDTO(
                preferences.theme(),
                preferences.defaultModel()
        );
    }

    public CollaboratorPreferences toDomain() {
        return new CollaboratorPreferences(theme, defaultModel);
    }
}