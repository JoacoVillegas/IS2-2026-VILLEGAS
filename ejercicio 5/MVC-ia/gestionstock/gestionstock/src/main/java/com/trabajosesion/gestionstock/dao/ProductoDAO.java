package com.trabajosesion.gestionstock.dao;

import com.trabajosesion.gestionstock.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** DAO de Producto. */
@Repository
public interface ProductoDAO extends JpaRepository<Producto, Long> {
    List<Producto> findByEliminadoFalse();
}
