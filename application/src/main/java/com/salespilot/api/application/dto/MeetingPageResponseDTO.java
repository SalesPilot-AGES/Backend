package com.salespilot.api.application.dto;

import org.springframework.data.domain.Page;

public record MeetingPageResponseDTO(
        Page<MeetingResponseDTO> meetingResponseDTO,
        SummaryResponseDTO summary) {
}
