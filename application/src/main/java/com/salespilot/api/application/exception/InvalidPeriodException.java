package com.salespilot.api.application.exception;

public class InvalidPeriodException extends RuntimeException{
    public InvalidPeriodException(){
        super("Período inválido");
    }
}
