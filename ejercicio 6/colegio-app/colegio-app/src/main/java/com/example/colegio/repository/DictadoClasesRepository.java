package com.example.colegio.repository;

import com.example.colegio.entity.DictadoClases;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictadoClasesRepository extends JpaRepository<DictadoClases, Long> {

    /** Todas las asignaciones (materia+curso) a cargo de un profesor. Usado para autorizacion fina. */
    List<DictadoClases> findByProfesor_Id(Long profesorId);
}
