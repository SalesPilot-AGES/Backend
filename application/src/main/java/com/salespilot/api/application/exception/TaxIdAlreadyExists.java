package com.salespilot.api.application.exception;

public class TaxIdAlreadyExists extends RuntimeException {

    public TaxIdAlreadyExists() {
        super("Tax id already exists");
    }
}
