package com.example.club.service;

import com.example.club.dto.ActividadDTO;
import com.example.club.entity.Actividad;
import java.util.List;

public interface ActividadService {

    List<ActividadDTO> listarActivas();

    ActividadDTO buscarPorId(Long id);

    ActividadDTO registrar(ActividadDTO dto);

    ActividadDTO actualizar(Long id, ActividadDTO dto);

    void eliminar(Long id);

    /** Uso interno de InscripcionServiceImpl. */
    Actividad obtenerEntidadPorId(Long id);

    /** Uso interno de InscripcionServiceImpl para validar cupos sin tocar InscripcionRepository directamente desde otro Service. */
    long contarInscriptosActivos(Long actividadId);
}
