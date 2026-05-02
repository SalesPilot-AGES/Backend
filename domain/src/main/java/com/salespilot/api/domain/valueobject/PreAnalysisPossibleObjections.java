package com.salespilot.api.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PreAnalysisPossibleObjections(
        @JsonProperty("possible_objections")
        List<String> possibleObjections
) {
}
