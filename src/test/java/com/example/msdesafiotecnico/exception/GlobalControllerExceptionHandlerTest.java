package com.example.msdesafiotecnico.exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalControllerExceptionHandlerTest {

    private GlobalControllerExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalControllerExceptionHandler();
    }

    @Test
    void testHandleNoContentException() {
        // Given
        NoContentException exception = new NoContentException("Dados não encontrados");

        // When
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNoContentException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("NO_CONTENT");
        assertThat(response.getBody().get("message")).isEqualTo("Dados não encontrados");
        assertThat(response.getBody().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleIntegrationException() {
        // Given
        IntegrationServiceRestException exception = new IntegrationServiceRestException("Erro de integração");

        // When
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIntegrationException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("INTEGRATION_ERROR");
        assertThat(response.getBody().get("message")).isEqualTo("Serviço temporariamente indisponível");
    }

    @Test
    void testHandleValidationException() {
        // Given
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        when(exception.getMessage()).thenReturn("Validation failed");

        // When
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().get("message")).isEqualTo("Parâmetros inválidos: Validation failed");
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Method argument not valid");

        // When
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMethodArgumentNotValid(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().get("message")).isEqualTo("Dados de entrada inválidos");
    }

    @Test
    void testHandleTypeMismatch() {
        // Given
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("anoCompra");
        when(exception.getMessage()).thenReturn("Type mismatch");

        // When
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleTypeMismatch(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("TYPE_MISMATCH");
        assertThat(response.getBody().get("message")).isEqualTo("Tipo de parâmetro inválido: anoCompra");
    }

    @Test
    void testHandleGenericException() {
        // Given
        Exception exception = new RuntimeException("Erro inesperado");

        // When
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("code")).isEqualTo("INTERNAL_ERROR");
        assertThat(response.getBody().get("message")).isEqualTo("Erro interno do servidor");
    }
}
