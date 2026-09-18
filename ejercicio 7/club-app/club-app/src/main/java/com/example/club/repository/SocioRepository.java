package com.example.club.repository;

import com.example.club.entity.Socio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    List<Socio> findByEliminadoFalse();
}
