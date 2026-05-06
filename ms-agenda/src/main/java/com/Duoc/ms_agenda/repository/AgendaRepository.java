package com.Duoc.ms_agenda.repository;

import com.Duoc.ms_agenda.model.Cita;
import com.Duoc.ms_agenda.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findAll();
    Optional<Cita> findById(Long id);
    // Búsquedas personalizadas muy útiles
    List<Cita> findByIdPaciente(Long idPaciente);
    List<Cita> findByEstadoCita(EstadoCita estadoCita);
}