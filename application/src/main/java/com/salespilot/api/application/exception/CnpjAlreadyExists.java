package com.salespilot.api.application.exception;

public class CnpjAlreadyExists extends RuntimeException {

    public CnpjAlreadyExists() {
        super("Cnpj already exists");
    }
}
