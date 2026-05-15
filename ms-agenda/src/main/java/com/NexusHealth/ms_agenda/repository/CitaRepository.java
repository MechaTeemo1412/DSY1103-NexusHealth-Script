package com.NexusHealth.ms_agenda.repository;

import com.NexusHealth.ms_agenda.model.Cita;
import com.NexusHealth.ms_agenda.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    // Consulta mágica de Spring Data JPA:
    // Busca citas entre dos fechas y que tengan un estado específico.
    // Equivalent SQL: SELECT * FROM citas WHERE fecha_hora BETWEEN ? AND ? AND estado = ?
    List<Cita> findByFechaHoraBetweenAndEstado(LocalDateTime inicio, LocalDateTime fin, EstadoCita estado);
}
