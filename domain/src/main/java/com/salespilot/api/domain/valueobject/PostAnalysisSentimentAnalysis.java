package com.salespilot.api.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostAnalysisSentimentAnalysis(
        String overall,
        double score
) {}
