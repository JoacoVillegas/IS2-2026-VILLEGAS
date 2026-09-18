package com.example.club.repository;

import com.example.club.entity.GrupoFamiliar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoFamiliarRepository extends JpaRepository<GrupoFamiliar, Long> {
    List<GrupoFamiliar> findByEliminadoFalse();
    Optional<GrupoFamiliar> findBySocioTitular_Id(Long socioId);
}
