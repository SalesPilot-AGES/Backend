package com.salespilot.api.domain.valueobject;

public record PostAnalysisActionItem(
        String text,
        boolean done
) implements java.io.Serializable {}
