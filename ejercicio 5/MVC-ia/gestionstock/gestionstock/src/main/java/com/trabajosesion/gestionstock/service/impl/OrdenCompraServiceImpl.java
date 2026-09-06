package com.trabajosesion.gestionstock.service.impl;

import com.trabajosesion.gestionstock.dao.OrdenCompraDAO;
import com.trabajosesion.gestionstock.dao.ProveedorDAO;
import com.trabajosesion.gestionstock.dao.ProductoDAO;
import com.trabajosesion.gestionstock.dto.ItemOrdenDTO;
import com.trabajosesion.gestionstock.dto.NuevaOrdenDTO;
import com.trabajosesion.gestionstock.dto.OrdenCompraDTO;
import com.trabajosesion.gestionstock.mapper.OrdenCompraMapper;
import com.trabajosesion.gestionstock.model.DetalleOrden;
import com.trabajosesion.gestionstock.model.Empleado;
import com.trabajosesion.gestionstock.model.EstadoOrden;
import com.trabajosesion.gestionstock.model.OrdenCompra;
import com.trabajosesion.gestionstock.model.Producto;
import com.trabajosesion.gestionstock.model.Proveedor;
import com.trabajosesion.gestionstock.service.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * SERVICE - Implementacion de la logica de negocio de OrdenCompra.
 *
 * Este metodo es el ejemplo mas completo del flujo pedido en el punto 14
 * del enunciado ("Formulario Thymeleaf -> ProductoDTO/NuevaOrdenDTO ->
 * Controller -> Service -> DTO->Entity -> DAO -> JPA -> MySQL"), porque
 * una unica operacion de negocio ("registrar una orden de compra") arma y
 * persiste en una sola transaccion: 1 OrdenCompra, N DetalleOrden y N
 * actualizaciones de Stock.
 *
 * @Transactional asegura que, si algo fallara a mitad de camino (por
 * ejemplo, un id de producto invalido en la linea 3 de 5), Hibernate
 * revierta TODO (la orden, los detalles ya creados y los incrementos de
 * stock ya aplicados), evitando dejar la base de datos a medio actualizar.
 */
@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraDAO ordenCompraDAO;
    private final ProveedorDAO proveedorDAO;
    private final ProductoDAO productoDAO;
    private final OrdenCompraMapper ordenCompraMapper;

    @Autowired
    public OrdenCompraServiceImpl(OrdenCompraDAO ordenCompraDAO, ProveedorDAO proveedorDAO,
                                   ProductoDAO productoDAO, OrdenCompraMapper ordenCompraMapper) {
        this.ordenCompraDAO = ordenCompraDAO;
        this.proveedorDAO = proveedorDAO;
        this.productoDAO = productoDAO;
        this.ordenCompraMapper = ordenCompraMapper;
    }

    @Override
    @Transactional
    public OrdenCompraDTO registrarOrden(Empleado empleado, NuevaOrdenDTO nuevaOrden) {
        Proveedor proveedor = proveedorDAO.findById(nuevaOrden.getIdProveedor())
                .orElseThrow(() -> new NoSuchElementException("Proveedor inexistente"));

        OrdenCompra orden = new OrdenCompra(empleado, proveedor);
        orden.registrarOrden(); // fija fechaEmision = hoy y estado = PENDIENTE (metodo de dominio de OrdenCompra)

        for (ItemOrdenDTO item : nuevaOrden.getItems()) {
            if (item.getCantidad() <= 0) {
                continue; // linea que el empleado dejo en 0: no genera detalle
            }
            Producto producto = productoDAO.findById(item.getProductoId())
                    .orElseThrow(() -> new NoSuchElementException("Producto inexistente: " + item.getProductoId()));

            DetalleOrden detalle = new DetalleOrden(producto, item.getCantidad(), item.getPrecioUnitario());
            orden.agregarDetalle(detalle); // composicion: vincula el detalle y recalcula subtotal/precioTotal

            aumentarStockDelProducto(producto, detalle);
        }

        orden.cambiarEstado(EstadoOrden.CONFIRMADA); // se confirma automaticamente al registrarse con exito
        OrdenCompra guardada = ordenCompraDAO.save(orden); // cascade=ALL persiste tambien los DetalleOrden
        return ordenCompraMapper.toDTO(guardada);
    }

    /**
     * Traduce DetalleOrden.getCantidadAIncrementar() (metodo de dominio
     * corregido, ver comentario en la entidad DetalleOrden) en una
     * actualizacion real de la entidad Stock del producto comprado.
     */
    private void aumentarStockDelProducto(Producto producto, DetalleOrden detalle) {
        producto.getStock().incrementarStock(detalle.getCantidadAIncrementar(), "COMPRA_A_PROVEEDOR");
        // No hace falta llamar a un DAO de Stock explicitamente: Stock
        // esta en cascade=ALL desde Producto, y Producto ya esta
        // "administrado" (managed) por Hibernate dentro de esta misma
        // transaccion (fue recuperado con productoDAO.findById(...) mas
        // arriba), asi que Hibernate detecta el cambio y lo persiste solo
        // al finalizar la transaccion (mecanismo de "dirty checking").
    }

    @Override
    public List<OrdenCompraDTO> listarHistorial() {
        return ordenCompraDAO.findAllByOrderByFechaEmisionDesc().stream()
                .map(ordenCompraMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void anularOrden(Long idOrdenCompra) {
        ordenCompraDAO.findById(idOrdenCompra).ifPresent(orden -> {
            orden.cambiarEstado(EstadoOrden.ANULADA);
            ordenCompraDAO.save(orden);
            // Nota de diseño: al anular NO se revierte el incremento de
            // stock ya aplicado. Se documenta como una simplificacion
            // deliberada para no sobrecargar el ejercicio (revertir stock
            // de forma segura requeriria validar que ese stock no se haya
            // vuelto a mover desde entonces); anular aca representa
            // "esta orden ya no es valida a efectos administrativos/
            // contables", no un rollback fisico de mercaderia.
        });
    }
}
