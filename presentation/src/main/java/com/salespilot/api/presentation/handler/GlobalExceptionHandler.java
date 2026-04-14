package com.salespilot.api.presentation.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.salespilot.api.application.exception.TaxIdAlreadyExists;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaxIdAlreadyExists.class)
    public ResponseEntity<Void> handleTaxIdAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
