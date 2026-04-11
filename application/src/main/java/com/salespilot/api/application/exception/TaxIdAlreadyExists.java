package com.salespilot.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TaxIdAlreadyExists extends RuntimeException {

    public TaxIdAlreadyExists() {
        super("Tax id already exists");
    }
}
