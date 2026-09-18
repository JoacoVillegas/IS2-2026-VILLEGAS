package com.example.club.repository;

import com.example.club.entity.Persona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findByEliminadoFalse();
    List<Persona> findByGrupoFamiliar_IdAndEliminadoFalse(Long grupoFamiliarId);
}
