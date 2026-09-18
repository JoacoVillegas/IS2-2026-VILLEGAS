package com.example.club.repository;

import com.example.club.entity.RegistroAcceso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {
    List<RegistroAcceso> findAllByOrderByFechaDescHoraEntradaDesc();
    List<RegistroAcceso> findByPersona_IdOrderByFechaDesc(Long personaId);
    /** Registro abierto (sin salida) mas reciente de una persona: usado para no permitir dos entradas sin salida previa. */
    List<RegistroAcceso> findByPersona_IdAndHoraSalidaIsNull(Long personaId);
}
