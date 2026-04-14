package com.salespilot.api.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CollaboratorPreferences(
        String theme,
        @JsonProperty("default_model")
        String defaultModel
) {}
