package com.example.colegio.repository;

import com.example.colegio.entity.Aula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByEliminadoFalse();
}
