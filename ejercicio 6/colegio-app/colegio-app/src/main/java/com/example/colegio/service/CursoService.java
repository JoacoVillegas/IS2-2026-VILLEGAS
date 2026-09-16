package com.example.colegio.service;

import com.example.colegio.dto.CursoDTO;
import com.example.colegio.entity.Aula;
import java.util.List;

/** "CursoService" gestiona la entidad Aula (ver Aula.java / CursoDTO.java para la justificacion del nombre). */
public interface CursoService {

    List<CursoDTO> listarActivos();

    CursoDTO buscarPorId(Long id);

    CursoDTO registrar(CursoDTO dto);

    CursoDTO actualizar(Long id, CursoDTO dto);

    void eliminar(Long id);

    /** Devuelve la entidad Aula: uso interno exclusivo de AlumnoServiceImpl / DictadoClasesServiceImpl. */
    Aula obtenerEntidadPorId(Long id);
}
