package com.NexusHealth.ms_pacientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Obligatorio para controlar la respuesta HTTP
import org.springframework.web.bind.annotation.ControllerAdvice; // Atrapa errores de todos los Controllers
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Se activa al lanzar ResourceNotFoundException
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now()); // Agrega trazabilidad temporal
        body.put("error", "No Encontrado");
        body.put("message", ex.getMessage());

        // Retorna un JSON estructurado con código 404
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
