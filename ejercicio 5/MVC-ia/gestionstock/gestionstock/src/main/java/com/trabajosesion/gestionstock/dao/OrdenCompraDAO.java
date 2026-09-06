package com.trabajosesion.gestionstock.dao;

import com.trabajosesion.gestionstock.model.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** DAO de OrdenCompra. */
@Repository
public interface OrdenCompraDAO extends JpaRepository<OrdenCompra, Long> {
    List<OrdenCompra> findAllByOrderByFechaEmisionDesc();
}
