package com.Duoc.ms_agenda.controller;

import com.Duoc.ms_agenda.model.Cita;
import com.Duoc.ms_agenda.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agenda")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<Cita>> listarAgendas(){
        return new ResponseEntity<>(agendaService.obtenerTodas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerAgenda(@PathVariable Long id){
        return agendaService.obtenerPorId(id)
                .map(cita -> new ResponseEntity<>(cita, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
