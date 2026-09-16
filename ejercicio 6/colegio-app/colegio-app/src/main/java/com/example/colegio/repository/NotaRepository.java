package com.example.colegio.repository;

import com.example.colegio.entity.Nota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByAlumno_Id(Long alumnoId);

    List<Nota> findByMateria_IdMateria(Long materiaId);

    /** Notas de alumnos que pertenecen a alguno de los cursos (aulas) indicados. */
    List<Nota> findByAlumno_Aula_IdAulaIn(List<Long> aulaIds);
}
