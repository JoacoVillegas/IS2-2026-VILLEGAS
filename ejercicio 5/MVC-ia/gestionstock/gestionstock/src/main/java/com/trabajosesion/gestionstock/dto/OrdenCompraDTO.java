package com.trabajosesion.gestionstock.dto;

import com.trabajosesion.gestionstock.model.EstadoOrden;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO de solo lectura que representa una OrdenCompra ya registrada, listo
 * para mostrarse en la vista de historial. Aplana los datos de Empleado y
 * Proveedor (solo sus nombres) en lugar de exponer las entidades
 * completas: la vista de historial no necesita, por ejemplo, el listado
 * de TODAS las ordenes de ese mismo proveedor (que es lo que traeria si
 * se expusiera la entidad Proveedor completa).
 */
public class OrdenCompraDTO {

    private Long idOrdenCompra;
    private LocalDate fechaEmision;
    private EstadoOrden estado;
    private BigDecimal precioTotal;
    private String nombreEmpleado;
    private String nombreProveedor;
    private List<DetalleOrdenDTO> detalles = new ArrayList<>();

    public OrdenCompraDTO() {
    }

    public Long getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Long idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public List<DetalleOrdenDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenDTO> detalles) {
        this.detalles = detalles;
    }
}
