package com.trabajosesion.sistemacompras.dao;

import com.trabajosesion.sistemacompras.model.Inventario;
import com.trabajosesion.sistemacompras.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO de la entidad Inventario (movimientos de stock).
 */
@Repository
public interface InventarioDAO extends JpaRepository<Inventario, Long> {

    /**
     * Devuelve todos los movimientos de un producto ordenados del mas
     * reciente al mas antiguo. Spring Data JPA arma automaticamente la
     * consulta "SELECT * FROM inventario WHERE producto_id = ? ORDER BY
     * fecha DESC" a partir del nombre del metodo.
     */
    List<Inventario> findByProductoOrderByFechaDesc(Producto producto);

    List<Inventario> findAllByOrderByFechaDesc();
}
