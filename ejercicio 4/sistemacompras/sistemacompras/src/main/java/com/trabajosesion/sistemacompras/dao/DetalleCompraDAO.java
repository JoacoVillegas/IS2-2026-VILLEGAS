package com.trabajosesion.sistemacompras.dao;

import com.trabajosesion.sistemacompras.model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO de la entidad DetalleCompra. En la practica, la mayoria de las
 * operaciones sobre detalles se realizan en cascada a traves de Compra
 * (gracias a cascade = CascadeType.ALL en Compra.detalles), pero se
 * mantiene este repositorio propio por completitud y porque el diagrama de
 * clases modela DetalleCompra como una entidad de primer nivel.
 */
@Repository
public interface DetalleCompraDAO extends JpaRepository<DetalleCompra, Long> {
}
