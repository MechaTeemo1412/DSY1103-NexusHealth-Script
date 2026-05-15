package com.NexusHealth.ms_auditoria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        return new ResponseEntity<>(
                Map.of("timestamp", LocalDateTime.now(), "error", "Fallo al procesar el registro de auditoría", "detalle", ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
