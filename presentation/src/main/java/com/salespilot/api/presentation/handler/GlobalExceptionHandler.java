package com.salespilot.api.presentation.handler;

import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.ClientNotFoundException;

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

    @ExceptionHandler(CollaboratorAlreadyExistsException.class)
    public ResponseEntity<Void> handleCollaboratorAlreadyExistsException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Void> handleCompanyNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(CollaboratorNotFoundException.class)
    public ResponseEntity<Void> handleCollaboratorNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Void> handleClientNotFoundExeption() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
