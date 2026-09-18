package com.example.club.repository;

import com.example.club.entity.Actividad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByEliminadoFalse();
}
