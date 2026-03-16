package com.salespilot.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SystemStatus {
    private final String status;
    private final String timestamp;
}
