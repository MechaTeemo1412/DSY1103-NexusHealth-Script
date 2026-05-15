package com.NexusHealth.ms_pacientes.controller;

import com.NexusHealth.ms_pacientes.model.Paciente;
import com.NexusHealth.ms_pacientes.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/pacientes")
public class PacienteController {
    @Autowired // Conecta el controlador con la capa de servicio
    private PacienteService service;

    @GetMapping // Define que este método responde a peticiones HTTP GET
    public ResponseEntity<List<Paciente>> listarTodos() {
        // Retorna HTTP 200 OK y la lista de pacientes
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/rut/{rut}") // Path variable semántico para búsqueda específica
    public ResponseEntity<Paciente> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(service.obtenerPorRut(rut));
    }
}
