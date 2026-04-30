package com.salespilot.api.application.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record MeetingPageResponseDTO(
        List<MeetingResponseDTO> content,
        long totalElements,
        int totalPages,
        SummaryResponseDTO summary
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
