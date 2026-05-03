package com.Duoc.ms_pacientes.service;

import com.Duoc.ms_pacientes.model.Paciente;
import com.Duoc.ms_pacientes.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Paciente>obtenerTodos(){
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> obtenerPorId(Long id){
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if(paciente.isPresent()){
            enviarLogAuditoria("CONSULTA_PACIENTE","Se consultó exitosamente el ID: "+id);
        }
        return paciente;
    }

    private void enviarLogAuditoria(String accion, String detalle){
        try {
            Map<String,String> body = new HashMap<>();
            body.put("usuario","sistema_pacientes");
            body.put("accion",accion);
            body.put("detalle",detalle);

            String urlAuditoria = "http://localhost:8085/api/v1/auditoria";
            restTemplate.postForEntity(urlAuditoria, body, String.class);
            System.out.println("Log enviado a ms-auditoria con éxito.");
        }catch (Exception e){
            System.err.println("Fallo en la comunicación con ms-auditoria"+e.getMessage());
        }
    }
}
