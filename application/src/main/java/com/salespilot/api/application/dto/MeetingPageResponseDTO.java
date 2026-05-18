package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder({"content", "totalElements", "totalPages", "summary", "message"})
public record MeetingPageResponseDTO(
        @JsonProperty("content") List<MeetingResponseDTO> content,
        @JsonProperty("totalElements") long totalElements,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("summary") SummaryResponseDTO summary,
        @JsonProperty("message") String message
) {
    public static MeetingPageResponseDTO from(Page<MeetingResponseDTO> page, SummaryResponseDTO summary, String message) {
        return new MeetingPageResponseDTO(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                summary,
                message
        );
    }
}
