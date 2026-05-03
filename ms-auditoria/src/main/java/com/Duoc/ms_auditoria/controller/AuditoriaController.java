package com.Duoc.ms_auditoria.controller;


import com.Duoc.ms_auditoria.model.Auditoria;
import com.Duoc.ms_auditoria.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {
    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @PostMapping
    public ResponseEntity<Auditoria> registrarEvento(@RequestBody Auditoria auditoria){
        auditoria.setFechaHora(LocalDateTime.now());
        Auditoria nuevoRegistro = auditoriaRepository.save(auditoria);
        return new ResponseEntity<>(nuevoRegistro,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Auditoria>> obtenerHistorial(){
        return new ResponseEntity<>(auditoriaRepository.findAll(),HttpStatus.OK);
    }
}
