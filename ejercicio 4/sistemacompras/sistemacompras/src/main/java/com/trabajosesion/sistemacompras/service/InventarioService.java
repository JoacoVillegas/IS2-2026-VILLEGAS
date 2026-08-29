package com.trabajosesion.sistemacompras.service;

import com.trabajosesion.sistemacompras.model.Inventario;
import com.trabajosesion.sistemacompras.model.Producto;

import java.util.List;

/**
 * SERVICE - Consulta del historial de movimientos de stock
 * (metodo registrarMovimiento() del diagrama se expone tambien aca para
 * altas manuales de stock, ademas de las bajas automaticas que genera
 * CompraService al confirmarse una compra).
 */
public interface InventarioService {

    List<Inventario> listarTodos();

    List<Inventario> listarPorProducto(Producto producto);

    Inventario registrarMovimiento(Producto producto, int cantidad, String tipoMovimiento);
}
