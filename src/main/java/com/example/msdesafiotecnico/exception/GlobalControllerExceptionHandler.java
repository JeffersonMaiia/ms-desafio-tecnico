package com.example.msdesafiotecnico.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Map<String, Object>> handleNoContentException(NoContentException ex) {
        log.warn("NoContentException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(createErrorResponse("NO_CONTENT", ex.getMessage()));
    }

    @ExceptionHandler(IntegrationServiceRestException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrationException(IntegrationServiceRestException ex) {
        log.error("Integration error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(createErrorResponse("INTEGRATION_ERROR", "Serviço temporariamente indisponível"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ConstraintViolationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createErrorResponse("VALIDATION_ERROR", "Parâmetros inválidos: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Method argument validation error: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createErrorResponse("VALIDATION_ERROR", "Dados de entrada inválidos"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch error: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createErrorResponse("TYPE_MISMATCH", "Tipo de parâmetro inválido: " + ex.getName()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("INTERNAL_ERROR", "Erro interno do servidor"));
    }

    private Map<String, Object> createErrorResponse(String code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("code", code);
        error.put("message", message);
        return error;
    }
}
