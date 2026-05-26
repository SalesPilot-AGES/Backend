package com.salespilot.api.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostAnalysisActionItem(
        String text,
        boolean done
) implements java.io.Serializable {}
