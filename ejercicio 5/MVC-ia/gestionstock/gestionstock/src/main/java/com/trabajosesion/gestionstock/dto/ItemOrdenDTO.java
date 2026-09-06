package com.trabajosesion.gestionstock.dto;

import java.math.BigDecimal;

/**
 * DTO de ENTRADA: representa una linea del formulario "Realizar compra"
 * (un producto + la cantidad a reponer + el precio unitario pactado con
 * el proveedor para esta orden). Viaja del Controller al Service dentro
 * de NuevaOrdenDTO; el Service es quien resuelve el id de producto contra
 * la entidad real (ProductoDAO) y construye el DetalleOrden definitivo.
 */
public class ItemOrdenDTO {

    private Long productoId;
    private int cantidad;
    private BigDecimal precioUnitario;

    public ItemOrdenDTO() {
    }

    public ItemOrdenDTO(Long productoId, int cantidad, BigDecimal precioUnitario) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
