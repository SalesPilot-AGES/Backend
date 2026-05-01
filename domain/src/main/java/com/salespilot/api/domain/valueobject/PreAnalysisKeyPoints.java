package com.salespilot.api.domain.valueobject;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PreAnalysisKeyPoints(
        @JsonProperty("key_points")
        List<String> keyPoints
) {

}
