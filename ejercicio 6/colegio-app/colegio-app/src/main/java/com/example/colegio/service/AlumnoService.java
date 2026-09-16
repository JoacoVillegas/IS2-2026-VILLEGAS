package com.example.colegio.service;

import com.example.colegio.dto.AlumnoDTO;
import com.example.colegio.entity.Alumno;
import java.util.List;

public interface AlumnoService {

    List<AlumnoDTO> listarActivos();

    /** Alumnos activos que pertenecen a alguno de los cursos indicados (usado por el profesor). */
    List<AlumnoDTO> listarPorAulas(List<Long> aulaIds);

    AlumnoDTO buscarPorId(Long id);

    AlumnoDTO registrar(AlumnoDTO dto);

    AlumnoDTO actualizar(Long id, AlumnoDTO dto);

    void eliminar(Long id);

    /** Uso interno de otros Services (NotaServiceImpl). */
    Alumno obtenerEntidadPorId(Long id);
}
