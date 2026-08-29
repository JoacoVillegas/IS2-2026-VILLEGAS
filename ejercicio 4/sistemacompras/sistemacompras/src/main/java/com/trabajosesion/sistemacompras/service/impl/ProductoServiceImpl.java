package com.trabajosesion.sistemacompras.service.impl;

import com.trabajosesion.sistemacompras.dao.InventarioDAO;
import com.trabajosesion.sistemacompras.dao.ProductoDAO;
import com.trabajosesion.sistemacompras.model.Inventario;
import com.trabajosesion.sistemacompras.model.Producto;
import com.trabajosesion.sistemacompras.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * SERVICE - Implementacion de la logica de negocio de Producto.
 *
 * Nota de diseño: este Service coordina DOS DAOs (ProductoDAO e
 * InventarioDAO). Esto es exactamente el tipo de coordinacion entre varias
 * entidades que debe vivir en el Service y NO en el Controller: el
 * Controller no deberia enterarse de que "registrar un producto" implica
 * ademas crear un primer movimiento de inventario.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;
    private final InventarioDAO inventarioDAO;

    @Autowired
    public ProductoServiceImpl(ProductoDAO productoDAO, InventarioDAO inventarioDAO) {
        this.productoDAO = productoDAO;
        this.inventarioDAO = inventarioDAO;
    }

    @Override
    public List<Producto> listarTodos() {
        return productoDAO.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoDAO.findById(id);
    }

    @Override
    @Transactional
    public Producto registrarProducto(String nombre, String descripcion, BigDecimal precio, int stockInicial) {
        Producto producto = new Producto(nombre, descripcion, precio);
        Producto guardado = productoDAO.save(producto);

        // Si se cargo un stock inicial, se registra como el primer
        // movimiento de inventario del producto (relacion de agregacion
        // Producto --- Inventario materializandose por primera vez).
        if (stockInicial > 0) {
            Inventario movimientoInicial = new Inventario(
                    LocalDateTime.now(), stockInicial, "ALTA_INICIAL", guardado);
            inventarioDAO.save(movimientoInicial);
        }
        return guardado;
    }

    @Override
    @Transactional
    public Producto editarProducto(Long id, String nombre, String descripcion, BigDecimal precio) {
        Producto producto = productoDAO.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el producto con id " + id));
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        return productoDAO.save(producto);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        productoDAO.deleteById(id);
    }

    @Override
    public int calcularStockActual(Producto producto) {
        return inventarioDAO.findByProductoOrderByFechaDesc(producto)
                .stream()
                .mapToInt(Inventario::getCantidad)
                .sum();
    }
}
