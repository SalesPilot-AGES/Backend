package com.salespilot.api.application.dto;

import java.util.List;

public record MeetingsBySellerResponseDto(
    List<SellerNameAndTotalMeetingsDto> data
) {
}
