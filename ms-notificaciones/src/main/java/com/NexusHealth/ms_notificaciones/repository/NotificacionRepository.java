package com.NexusHealth.ms_notificaciones.repository;

import com.NexusHealth.ms_notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
}
