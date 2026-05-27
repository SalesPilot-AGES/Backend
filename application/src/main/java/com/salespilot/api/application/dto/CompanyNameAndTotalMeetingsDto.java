package com.salespilot.api.application.dto;

public record CompanyNameAndTotalMeetingsDto(
    String companyName,
    Long total
) {
}
