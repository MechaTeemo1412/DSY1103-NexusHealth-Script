package com.Duoc.ms_comunicaciones.controller;


import com.Duoc.ms_comunicaciones.model.Plantilla;
import com.Duoc.ms_comunicaciones.service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class PlantillaController {

    @Autowired
    private NotificacionesService notificacionesService;

    @GetMapping
    public ResponseEntity<List<Plantilla>> listarPlantillas(){
        return new ResponseEntity<>(notificacionesService.obtenerPlantillas(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Plantilla> obtenerPlantillaPorId(@PathVariable Long id){
        return notificacionesService.obtenerPlantillaPorId(id)
                .map(plantilla -> new ResponseEntity<>(plantilla,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
