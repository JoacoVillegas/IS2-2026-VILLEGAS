package com.example.club.service;

import com.example.club.dto.InscripcionDTO;
import java.util.List;

public interface InscripcionService {

    List<InscripcionDTO> listarPorActividad(Long actividadId);

    List<InscripcionDTO> listarPorSocio(Long socioId);

    /**
     * Corresponde al metodo "inscribirSocio()" del diagrama original de
     * Actividad; se implementa aqui (Service) y no en la entidad, porque
     * requiere validar el cupo disponible (consultando ActividadService) y
     * persistir una nueva fila -- responsabilidades de negocio, no de una
     * entidad JPA.
     */
    InscripcionDTO inscribirSocio(Long socioId, Long actividadId);

    /** Corresponde a "cancelarInscripcion()" del diagrama original. */
    void cancelarInscripcion(Long inscripcionId);
}
