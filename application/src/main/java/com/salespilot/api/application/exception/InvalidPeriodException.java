package com.salespilot.api.application.exception;

import java.time.LocalDate;

public class InvalidPeriodException extends RuntimeException{
    public InvalidPeriodException(LocalDate startDate, LocalDate endDate){
        super("Período inválido: " + startDate + ", " + endDate);
    }
}
