package com.trabajosesion.sistemacompras.service.impl;

import com.trabajosesion.sistemacompras.dao.InventarioDAO;
import com.trabajosesion.sistemacompras.model.Inventario;
import com.trabajosesion.sistemacompras.model.Producto;
import com.trabajosesion.sistemacompras.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVICE - Implementacion de la logica de negocio de Inventario.
 * Corresponde al metodo "registrarMovimiento()" de la clase Inventario en
 * el diagrama de clases.
 */
@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioDAO inventarioDAO;

    @Autowired
    public InventarioServiceImpl(InventarioDAO inventarioDAO) {
        this.inventarioDAO = inventarioDAO;
    }

    @Override
    public List<Inventario> listarTodos() {
        return inventarioDAO.findAllByOrderByFechaDesc();
    }

    @Override
    public List<Inventario> listarPorProducto(Producto producto) {
        return inventarioDAO.findByProductoOrderByFechaDesc(producto);
    }

    @Override
    @Transactional
    public Inventario registrarMovimiento(Producto producto, int cantidad, String tipoMovimiento) {
        Inventario movimiento = new Inventario(LocalDateTime.now(), cantidad, tipoMovimiento, producto);
        return inventarioDAO.save(movimiento);
    }
}
