package com.salespilot.api.presentation.handler;

import com.salespilot.api.application.dto.ApiResponse;
import com.salespilot.api.application.exception.ClientNotFoundException;
import com.salespilot.api.application.exception.CollaboratorAlreadyExistsException;
import com.salespilot.api.application.exception.CollaboratorNotFoundException;
import com.salespilot.api.application.exception.CompanyNotFoundException;
import com.salespilot.api.application.exception.ForbiddenException;
import com.salespilot.api.application.exception.InvalidCollaboratorRoleException;
import com.salespilot.api.application.exception.InvalidCredentialsException;
import com.salespilot.api.application.exception.InvalidPeriodException;
import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.application.exception.MeetingPostAnalysisNotFoundException;
import com.salespilot.api.application.exception.RefreshTokenInvalidException;
import com.salespilot.api.application.exception.TaxIdAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaxIdAlreadyExists.class)
    public ResponseEntity<ApiResponse<Void>> handleTaxIdAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.message("CNPJ já cadastrado"));
    }

    @ExceptionHandler(CollaboratorAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCollaboratorAlreadyExistsException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.message("Colaborador já existe nesta empresa com este e-mail"));
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCompanyNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.message("Empresa não encontrada"));
    }

    @ExceptionHandler(CollaboratorNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCollaboratorNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.message("Colaborador não encontrado"));
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleClientNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.message("Cliente não encontrado"));
    }

    @ExceptionHandler(MeetingNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMeetingNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.message("Reunião não encontrada"));
    }

    @ExceptionHandler(MeetingPostAnalysisNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMeetingPostAnalysisNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.message("Pós-análise da reunião não encontrada"));
    }

    @ExceptionHandler(InvalidCollaboratorRoleException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCollaboratorRoleExeption() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.message("Role inválido para colaborador"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.message("Dados inválidos"));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidationException() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.message("Dados inválidos"));
    }

    @ExceptionHandler(InvalidPeriodException.class)
    public ResponseEntity<Void> handleInvalidPeriodException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Void> handleInvalidCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<Void> handleRefreshTokenInvalidException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({AccessDeniedException.class, ForbiddenException.class})
    public ResponseEntity<ApiResponse<Void>> handleForbiddenException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.message("Acesso negado"));
    }
}
