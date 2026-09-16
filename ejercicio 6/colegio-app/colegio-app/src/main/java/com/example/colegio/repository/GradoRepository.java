package com.example.colegio.repository;

import com.example.colegio.entity.Grado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradoRepository extends JpaRepository<Grado, Long> {
    List<Grado> findByEliminadoFalse();
}
