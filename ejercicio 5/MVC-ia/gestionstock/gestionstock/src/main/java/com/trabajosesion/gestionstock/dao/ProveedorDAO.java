package com.trabajosesion.gestionstock.dao;

import com.trabajosesion.gestionstock.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** DAO de Proveedor. */
@Repository
public interface ProveedorDAO extends JpaRepository<Proveedor, Long> {
    /** Lista solo los proveedores activos (baja logica): usado en los listados y en el combo de "Realizar compra". */
    List<Proveedor> findByEliminadoFalse();
}
