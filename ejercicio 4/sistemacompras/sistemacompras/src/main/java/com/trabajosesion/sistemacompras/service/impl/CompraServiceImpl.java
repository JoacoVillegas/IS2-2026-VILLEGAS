package com.trabajosesion.sistemacompras.service.impl;

import com.trabajosesion.sistemacompras.dao.CompraDAO;
import com.trabajosesion.sistemacompras.dao.InventarioDAO;
import com.trabajosesion.sistemacompras.dao.ProductoDAO;
import com.trabajosesion.sistemacompras.model.Compra;
import com.trabajosesion.sistemacompras.model.DetalleCompra;
import com.trabajosesion.sistemacompras.model.Inventario;
import com.trabajosesion.sistemacompras.model.Producto;
import com.trabajosesion.sistemacompras.model.Usuario;
import com.trabajosesion.sistemacompras.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * SERVICE - Implementacion de la logica de negocio de Compra.
 *
 * Este es el ejemplo mas claro del flujo completo pedido en el punto 9 del
 * enunciado (Usuario -> Vista -> Controller -> Service -> DAO -> JPA ->
 * MySQL), porque una sola operacion de negocio ("registrar una compra")
 * termina escribiendo en TRES tablas distintas (compras, detalle_compra e
 * inventario) de forma coordinada.
 *
 * @Transactional a nivel de metodo es especialmente importante aca: si al
 * procesar el tercer producto de una compra de cinco algo fallara, Spring
 * revierte TODO (la compra, los detalles ya creados y los movimientos de
 * inventario ya generados), evitando dejar la base de datos en un estado
 * inconsistente (por ejemplo, una compra "a medias" o stock descontado sin
 * una compra asociada).
 */
@Service
public class CompraServiceImpl implements CompraService {

    private final CompraDAO compraDAO;
    private final ProductoDAO productoDAO;
    private final InventarioDAO inventarioDAO;

    @Autowired
    public CompraServiceImpl(CompraDAO compraDAO, ProductoDAO productoDAO, InventarioDAO inventarioDAO) {
        this.compraDAO = compraDAO;
        this.productoDAO = productoDAO;
        this.inventarioDAO = inventarioDAO;
    }

    @Override
    @Transactional
    public Compra registrarCompra(Usuario usuario, Map<Long, Integer> cantidadesPorProductoId) {
        Compra compra = new Compra(usuario);
        compra.registrarCompra(); // fija fechaCompra = ahora (metodo de dominio de Compra)

        for (Map.Entry<Long, Integer> entrada : cantidadesPorProductoId.entrySet()) {
            Long idProducto = entrada.getKey();
            int cantidad = entrada.getValue();
            if (cantidad <= 0) {
                continue; // el usuario dejo la cantidad en 0: no genera detalle
            }

            Producto producto = productoDAO.findById(idProducto)
                    .orElseThrow(() -> new NoSuchElementException("Producto inexistente: " + idProducto));

            DetalleCompra detalle = new DetalleCompra(producto, cantidad, producto.getPrecio());
            // agregarDetalle() es el metodo de composicion definido en
            // Compra: vincula el detalle con esta compra y recalcula el
            // precioTotal automaticamente.
            compra.agregarDetalle(detalle);

            disminuirStock(detalle);
        }

        // Al guardar la Compra, gracias a cascade = CascadeType.ALL en
        // Compra.detalles, Hibernate guarda en la misma transaccion todos
        // los DetalleCompra que se agregaron en memoria.
        return compraDAO.save(compra);
    }

    /**
     * Traduce DetalleCompra.getCantidadADescontar() (metodo de dominio,
     * ver comentario en la entidad DetalleCompra) en un movimiento real de
     * Inventario con cantidad negativa, dejando registrado el motivo
     * ("VENTA") para el historial de auditoria.
     */
    private void disminuirStock(DetalleCompra detalle) {
        Inventario movimiento = new Inventario(
                LocalDateTime.now(),
                detalle.getCantidadADescontar(),
                "VENTA",
                detalle.getProducto());
        inventarioDAO.save(movimiento);
    }

    @Override
    public List<Compra> historialDeCompras(Usuario usuario) {
        return compraDAO.findByUsuarioOrderByFechaCompraDesc(usuario);
    }

    @Override
    @Transactional
    public void anularCompra(Long idCompra) {
        compraDAO.findById(idCompra).ifPresent(compra -> {
            compra.anularCompra();
            compraDAO.save(compra);
        });
    }
}
