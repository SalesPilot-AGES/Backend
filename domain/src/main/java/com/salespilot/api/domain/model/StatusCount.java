package com.salespilot.api.domain.model;

public record StatusCount(
    boolean active,
    Long total
) {
}
