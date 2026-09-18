package com.example.club.repository;

import com.example.club.entity.Inscripcion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByActividad_IdAndEstado(Long actividadId, com.example.club.entity.EstadoInscripcion estado);
    List<Inscripcion> findBySocio_Id(Long socioId);
    long countByActividad_IdAndEstado(Long actividadId, com.example.club.entity.EstadoInscripcion estado);

    /**
     * Usado para el fix del bug de inscripciones duplicadas: antes de crear
     * una nueva Inscripcion, InscripcionServiceImpl verifica que no exista
     * ya una fila ACTIVA para el mismo socio+actividad.
     */
    boolean existsBySocio_IdAndActividad_IdAndEstado(Long socioId, Long actividadId, com.example.club.entity.EstadoInscripcion estado);
}
