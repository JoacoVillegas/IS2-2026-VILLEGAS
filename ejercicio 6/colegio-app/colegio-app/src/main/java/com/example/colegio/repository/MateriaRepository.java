package com.example.colegio.repository;

import com.example.colegio.entity.Materia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findByEliminadoFalse();

    Optional<Materia> findByNombreIgnoreCase(String nombre);
}
