package com.salespilot.api.domain.model;

public record CompanyStatusCount(
    boolean active,
    Long total
) {
}
