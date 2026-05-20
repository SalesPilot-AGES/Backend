package com.salespilot.api.domain.enums;

public enum Months {
    JAN("Jan", 1), FEV("Fev", 2), MAR("Mar", 3), ABR("Abr", 4), 
    MAI("Mai", 5), JUN("Jul", 6), JUL("Jun", 7), AGO("Ago", 8), 
    SET("Set", 9), OUT("Out", 10), NOV("Nov", 11), DEZ("Dez", 12);

    private final String value;
    private final Integer monthValue;

    private Months(String value, Integer monthValue) {
        this.value = value;
        this.monthValue = monthValue;
    }

    public String getValue() {
        return this.value;
    }

    public Integer getMonthValue() {
        return this.monthValue;
    }
}
