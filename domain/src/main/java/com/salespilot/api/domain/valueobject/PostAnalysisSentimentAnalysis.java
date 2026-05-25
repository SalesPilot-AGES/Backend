package com.salespilot.api.domain.valueobject;

public record PostAnalysisSentimentAnalysis(
        String overall,
        double score
) implements java.io.Serializable {}
