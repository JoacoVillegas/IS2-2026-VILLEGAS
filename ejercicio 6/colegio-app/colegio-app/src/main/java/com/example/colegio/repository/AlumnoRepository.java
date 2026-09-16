package com.example.colegio.repository;

import com.example.colegio.entity.Alumno;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    List<Alumno> findByEliminadoFalse();

    List<Alumno> findByAula_IdAulaAndEliminadoFalse(Long aulaId);

    /** Cuenta cuantos alumnos activos tiene un aula: usado por Aula.obtenerCantidadAlumnos(). */
    long countByAula_IdAulaAndEliminadoFalse(Long aulaId);
}
