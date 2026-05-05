package com.Duoc.ms_agenda.service;

import com.Duoc.ms_agenda.model.Cita;
import com.Duoc.ms_agenda.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerPorId(Long id) {
        Optional<Cita> cita = citaRepository.findById(id);
        if (cita.isPresent()) {
            enviarLogAuditoria("CONSULTA_CITA", "Se consultó la cita ID: " + id);
        }
        return cita;
    }

    public Cita agendarCita(Cita cita) {
        if (cita.getExamenes() != null) {
            cita.getExamenes().forEach(examen -> examen.setCita(cita));
        }
        Cita nuevaCita = citaRepository.save(cita);
        enviarLogAuditoria("CREACION_CITA", "Se agendó una nueva cita ID: " + nuevaCita.getId());
        return nuevaCita;
    }

    private void enviarLogAuditoria(String accion, String detalle) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("usuario", "sistema_agenda");
            body.put("accion", accion);
            body.put("detalle", detalle);

            String urlAuditoria = "http://localhost:8085/api/v1/auditoria";
            restTemplate.postForEntity(urlAuditoria, body, String.class);
            System.out.println("Log enviado a ms-auditoria.");
        } catch (Exception e) {
            System.err.println("Fallo al conectar con ms-auditoria: " + e.getMessage());
        }
    }
}
