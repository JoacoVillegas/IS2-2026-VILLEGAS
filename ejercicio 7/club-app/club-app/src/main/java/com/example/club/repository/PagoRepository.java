package com.example.club.repository;

import com.example.club.entity.Pago;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByGrupoFamiliar_IdOrderByFechaDesc(Long grupoFamiliarId);
    List<Pago> findAllByOrderByFechaDesc();
}
