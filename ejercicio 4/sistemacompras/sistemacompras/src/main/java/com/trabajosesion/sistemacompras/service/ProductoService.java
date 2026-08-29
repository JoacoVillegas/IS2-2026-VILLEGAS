package com.trabajosesion.sistemacompras.service;

import com.trabajosesion.sistemacompras.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SERVICE - Logica de negocio de Producto (equivale a los metodos
 * registrarProducto/editarProducto/eliminarProducto del diagrama de
 * clases, expresados aca como operaciones CRUD explicitas).
 */
public interface ProductoService {

    List<Producto> listarTodos();

    Optional<Producto> buscarPorId(Long id);

    Producto registrarProducto(String nombre, String descripcion, BigDecimal precio, int stockInicial);

    Producto editarProducto(Long id, String nombre, String descripcion, BigDecimal precio);

    void eliminarProducto(Long id);

    /** Calcula el stock actual sumando todos los movimientos de Inventario del producto. */
    int calcularStockActual(Producto producto);
}
