package com.salespilot.api.presentation.handler;

import com.salespilot.api.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<Void> handleClientNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MeetingNotFoundException.class)
    public ResponseEntity<Void> handleMeetingNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MeetingPostAnalysisNotFoundException.class)
    public ResponseEntity<Void> handleMeetingPostAnalysisNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MeetingPreAnalysisNotFoundException.class)
    public ResponseEntity<Void> handleMeetingPreAnalysisNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
