package com.salespilot.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CnpjAlreadyExists extends RuntimeException {

    public CnpjAlreadyExists() {
        super("Cnpj already exists");
    }
}
