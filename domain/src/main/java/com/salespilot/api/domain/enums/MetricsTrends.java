package com.salespilot.api.domain.enums;

public enum MetricsTrends {
    UP("up"), NEUTRAL("neutral"), DOWN("down");

    private final String value;

    private MetricsTrends(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
