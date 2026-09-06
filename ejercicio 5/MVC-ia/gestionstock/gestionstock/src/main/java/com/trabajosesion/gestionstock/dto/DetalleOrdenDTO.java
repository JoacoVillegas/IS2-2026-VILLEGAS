package com.trabajosesion.gestionstock.dto;

import java.math.BigDecimal;

/**
 * DTO de solo lectura para mostrar una linea de OrdenCompra ya registrada
 * en la vista de historial (ordenes/list.html). Aplana la relacion
 * DetalleOrden -> Producto en un unico campo "nombreProducto" para que la
 * vista no tenga que navegar entidades ni arriesgarse a una
 * LazyInitializationException fuera de una transaccion.
 */
public class DetalleOrdenDTO {

    private String nombreProducto;
    private int cantidadProducto;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalLinea;

    public DetalleOrdenDTO() {
    }

    public DetalleOrdenDTO(String nombreProducto, int cantidadProducto, BigDecimal precioUnitario) {
        this.nombreProducto = nombreProducto;
        this.cantidadProducto = cantidadProducto;
        this.precioUnitario = precioUnitario;
        this.subtotalLinea = precioUnitario.multiply(BigDecimal.valueOf(cantidadProducto));
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }
}
