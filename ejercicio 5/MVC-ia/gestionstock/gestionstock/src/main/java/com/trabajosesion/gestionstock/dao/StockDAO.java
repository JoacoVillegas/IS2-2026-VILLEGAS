package com.trabajosesion.gestionstock.dao;

import com.trabajosesion.gestionstock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** DAO de Stock. La mayoria de sus operaciones ocurren en cascada a traves de Producto (ver Producto.stock, cascade=ALL). */
@Repository
public interface StockDAO extends JpaRepository<Stock, Long> {
}
