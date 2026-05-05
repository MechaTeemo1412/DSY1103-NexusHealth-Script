package com.Duoc.ms_agenda.controller;

import com.Duoc.ms_agenda.model.Cita;
import com.Duoc.ms_agenda.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agenda")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> listarCitas() {
        return new ResponseEntity<>(citaService.obtenerTodas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerPorId(id)
                .map(cita -> new ResponseEntity<>(cita, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Cita> agendarCita(@RequestBody Cita cita) {
        Cita nuevaCita = citaService.agendarCita(cita);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }
}
