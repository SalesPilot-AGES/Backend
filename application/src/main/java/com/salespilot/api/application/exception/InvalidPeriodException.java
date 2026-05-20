package com.salespilot.api.application.exception;

import java.time.LocalDate;

public class InvalidPeriodException extends RuntimeException{
    public InvalidPeriodException(LocalDate start, LocalDate end){
        super("Período inválido: " + start + ", " + end);
    }
}
