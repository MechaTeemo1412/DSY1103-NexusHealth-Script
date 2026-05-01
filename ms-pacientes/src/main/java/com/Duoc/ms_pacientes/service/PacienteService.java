package com.Duoc.ms_pacientes.service;

import com.Duoc.ms_pacientes.model.Paciente;
import com.Duoc.ms_pacientes.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente>obtenerTodos(){
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> obtenerPorId(Long id){
        return pacienteRepository.findById(id);
    }
}
