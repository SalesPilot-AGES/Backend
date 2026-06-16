package com.salespilot.api.application.dto;

import java.util.List;

public record TopFiveCompanyByMeetingTotalResponseDto(
    List<CompanyNameAndTotalMeetingsDto> data
) {
}
