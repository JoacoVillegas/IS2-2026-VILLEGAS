package com.trabajosesion.gestionstock.service;

import com.trabajosesion.gestionstock.dto.NuevaOrdenDTO;
import com.trabajosesion.gestionstock.dto.OrdenCompraDTO;
import com.trabajosesion.gestionstock.model.Empleado;

import java.util.List;

/**
 * SERVICE - Logica de negocio de OrdenCompra. Es el Service que mas
 * entidades coordina (Empleado, Proveedor, Producto, OrdenCompra,
 * DetalleOrden, Stock), exactamente el tipo de orquestacion entre varias
 * entidades que NO debe vivir en el Controller.
 */
public interface OrdenCompraService {

    /**
     * Registra una orden de compra completa: crea la OrdenCompra, un
     * DetalleOrden por cada linea con cantidad > 0, e incrementa el Stock
     * del Producto correspondiente en cada linea.
     */
    OrdenCompraDTO registrarOrden(Empleado empleado, NuevaOrdenDTO nuevaOrden);

    List<OrdenCompraDTO> listarHistorial();

    void anularOrden(Long idOrdenCompra);
}
