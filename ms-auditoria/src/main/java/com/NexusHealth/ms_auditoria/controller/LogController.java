package com.NexusHealth.ms_auditoria.controller;

import com.NexusHealth.ms_auditoria.dto.LogAuditoriaDTO;
import com.NexusHealth.ms_auditoria.model.Log;
import com.NexusHealth.ms_auditoria.service.LogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auditoria")
public class LogController {
    @Autowired
    private LogService service;

    @PostMapping("/registro")
    public ResponseEntity<Log> registrar(@Valid @RequestBody LogAuditoriaDTO dto) {
        // Devuelve 201 Created confirmando que el log fue guardado correctamente
        return new ResponseEntity<>(service.registrarEvento(dto), HttpStatus.CREATED);
    }
}
