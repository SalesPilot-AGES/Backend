package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder({"content", "totalElements", "totalPages", "summary"})
public record MeetingPageResponseDTO(
        @JsonProperty("content") List<MeetingResponseDTO> content,
        @JsonProperty("totalElements") long totalElements,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("summary") SummaryResponseDTO summary
) {
    public static MeetingPageResponseDTO from(Page<MeetingResponseDTO> page, SummaryResponseDTO summary) {
        return new MeetingPageResponseDTO(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                summary
        );
    }
}
