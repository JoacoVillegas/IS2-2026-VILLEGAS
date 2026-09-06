package com.trabajosesion.gestionstock.mapper;

import com.trabajosesion.gestionstock.dto.DetalleOrdenDTO;
import com.trabajosesion.gestionstock.dto.OrdenCompraDTO;
import com.trabajosesion.gestionstock.model.DetalleOrden;
import com.trabajosesion.gestionstock.model.OrdenCompra;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MAPPER de OrdenCompra. A diferencia de ProductoMapper/ProveedorMapper,
 * este mapper es SOLO de lectura (Entity -> DTO): el alta de una orden es
 * un proceso mas complejo que "convertir un DTO en una entidad" (hay que
 * resolver el Empleado de la sesion, el Proveedor elegido y cada
 * Producto de cada linea desde sus ids), por lo que esa construccion se
 * hace directamente en OrdenCompraServiceImpl y no se modela aca como un
 * mapeo generico.
 */
@Component
public class OrdenCompraMapper {

    public OrdenCompraDTO toDTO(OrdenCompra orden) {
        OrdenCompraDTO dto = new OrdenCompraDTO();
        dto.setIdOrdenCompra(orden.getIdOrdenCompra());
        dto.setFechaEmision(orden.getFechaEmision());
        dto.setEstado(orden.getEstado());
        dto.setPrecioTotal(orden.getPrecioTotal());
        dto.setNombreEmpleado(orden.getEmpleado().getNombre());
        dto.setNombreProveedor(orden.getProveedor().getRazonSocial());
        dto.setDetalles(toDetalleDTOList(orden.getDetalles()));
        return dto;
    }

    private List<DetalleOrdenDTO> toDetalleDTOList(List<DetalleOrden> detalles) {
        return detalles.stream()
                .map(d -> new DetalleOrdenDTO(d.getProducto().getNombre(), d.getCantidadProducto(), d.getPrecioUnitario()))
                .toList();
    }
}
