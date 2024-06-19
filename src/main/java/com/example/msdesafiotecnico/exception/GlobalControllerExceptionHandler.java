package com.example.msdesafiotecnico.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoContentException.class)
    public void handleNoContentException(NoContentException ex) {
        log.warn("Response status NO_CONTENT, motivo: {}",ex.getMessage());
    }
}
