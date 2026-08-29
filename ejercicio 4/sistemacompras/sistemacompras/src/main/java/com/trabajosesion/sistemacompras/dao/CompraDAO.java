package com.trabajosesion.sistemacompras.dao;

import com.trabajosesion.sistemacompras.model.Compra;
import com.trabajosesion.sistemacompras.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO de la entidad Compra.
 */
@Repository
public interface CompraDAO extends JpaRepository<Compra, Long> {

    /** Historial de compras de un usuario, de la mas reciente a la mas antigua. */
    List<Compra> findByUsuarioOrderByFechaCompraDesc(Usuario usuario);
}
