package com.Duoc.ms_pacientes.repository;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import com.Duoc.ms_pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Long> {
    Optional<Paciente>findByRut(String rut);
}
