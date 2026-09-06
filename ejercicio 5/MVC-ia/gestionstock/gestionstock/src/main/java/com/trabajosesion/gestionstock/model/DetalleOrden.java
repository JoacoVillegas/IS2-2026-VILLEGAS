package com.trabajosesion.gestionstock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "DetalleOrden".
 *
 * COMPOSICION: lado "parte" de OrdenCompra (clave foranea "orden_id").
 *
 * ASOCIACION AGREGADA (no estaba en el diagrama original): DetalleOrden
 * (muchos) --- (1) Producto. Sin esta relacion, un detalle de orden no
 * tendria forma de saber QUE producto se esta reponiendo; el diagrama
 * original solo lo vinculaba con "Stock", lo cual no alcanza para
 * identificar el producto en si.
 *
 * METODO CORREGIDO: el diagrama original llama a este metodo
 * "disminuirStock()". Se renombra a "aumentarStock()" porque, segun el
 * enunciado, una OrdenCompra es una compra a un proveedor MAYORISTA para
 * REPONER stock (el stock de la empresa debe subir, no bajar, cuando se
 * confirma una orden de compra). Mantener el nombre original habria sido
 * arrastrar un error logico del diagrama hacia el codigo.
 */
@Entity
@Table(name = "detalle_orden")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @Column(name = "cantidad_producto", nullable = false)
    private int cantidadProducto;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenCompra ordenCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public DetalleOrden() {
    }

    public DetalleOrden(Producto producto, int cantidadProducto, BigDecimal precioUnitario) {
        this.producto = producto;
        this.cantidadProducto = cantidadProducto;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Metodo de dominio (ver comentario de la clase sobre la correccion
     * de nombre). Devuelve la cantidad que hay que sumarle al Stock del
     * producto de este detalle; la operacion real sobre la entidad Stock
     * la realiza OrdenCompraServiceImpl, porque coordinar Producto+Stock
     * es responsabilidad de la capa Service, no del Model.
     */
    public int getCantidadAIncrementar() {
        return this.cantidadProducto;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
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

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
