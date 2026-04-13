package com.salespilot.api.presentation.dto;

import com.salespilot.api.domain.valueobject.CollaboratorPreferences;

public record CollaboratorPreferencesDTO(
        String theme,
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
