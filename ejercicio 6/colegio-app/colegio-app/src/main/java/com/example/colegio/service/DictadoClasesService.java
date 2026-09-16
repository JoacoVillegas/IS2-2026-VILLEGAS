package com.example.colegio.service;

import com.example.colegio.entity.DictadoClases;
import java.util.List;

/**
 * Servicio de la clase de asociacion DictadoClases (ver DictadoClases.java).
 * Se expone principalmente para uso INTERNO de otros Services (por ejemplo
 * NotaServiceImpl, para saber que cursos/materias tiene asignados un
 * profesor), respetando la regla "Service A -> Service B -> Repository B".
 */
public interface DictadoClasesService {

    List<DictadoClases> listarAsignacionesDeProfesor(Long profesorId);

    DictadoClases asignarProfesor(Long profesorId, Long materiaId, Long aulaId, java.time.LocalDate anioLectivo);
}
