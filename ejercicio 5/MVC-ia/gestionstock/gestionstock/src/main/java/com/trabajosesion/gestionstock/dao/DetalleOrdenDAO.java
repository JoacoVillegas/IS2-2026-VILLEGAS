package com.trabajosesion.gestionstock.dao;

import com.trabajosesion.gestionstock.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** DAO de DetalleOrden. Sus operaciones normalmente viajan en cascada junto con OrdenCompra (cascade=ALL, orphanRemoval=true). */
@Repository
public interface DetalleOrdenDAO extends JpaRepository<DetalleOrden, Long> {
}
