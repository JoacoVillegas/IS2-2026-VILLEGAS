package com.example.colegio.service;

import com.example.colegio.dto.NotaDTO;
import java.util.List;

public interface NotaService {

    List<NotaDTO> listarPorAlumno(Long alumnoId);

    /** Notas visibles para un profesor: solo de los cursos/materias que tiene asignados. */
    List<NotaDTO> listarVisiblesParaProfesor(Long profesorId);

    NotaDTO registrar(NotaDTO dto);

    void eliminar(Long id);
}
