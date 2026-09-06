package com.trabajosesion.gestionstock.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "OrdenCompra".
 *
 * Representa una orden de compra que un Empleado emite a un Proveedor
 * mayorista para reponer stock (segun el enunciado original del
 * ejercicio). Es el lado "muchos" de dos asociaciones @ManyToOne
 * (Empleado y Proveedor son cada uno el lado "1"), y el lado "dueño" (1)
 * de la COMPOSICION con DetalleOrden (los detalles no tienen sentido de
 * existir sin la orden que los contiene).
 */
@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrdenCompra;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "precio_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioTotal = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleOrden> detalles = new ArrayList<>();

    public OrdenCompra() {
    }

    public OrdenCompra(Empleado empleado, Proveedor proveedor) {
        this.empleado = empleado;
        this.proveedor = proveedor;
    }

    /** Metodo de dominio del diagrama: fija la fecha de emision al momento de registrar la orden. */
    public void registrarOrden() {
        this.fechaEmision = LocalDate.now();
        this.estado = EstadoOrden.PENDIENTE;
    }

    /** Metodo de dominio del diagrama (COMPOSICION): agrega un detalle y recalcula los totales. */
    public void agregarDetalle(DetalleOrden detalle) {
        detalle.setOrdenCompra(this);
        this.detalles.add(detalle);
        calcularTotal();
    }

    /** Metodo de dominio del diagrama: recorre los detalles y recalcula subtotal/precioTotal. */
    public void calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleOrden d : this.detalles) {
            total = total.add(d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidadProducto())));
        }
        this.subtotal = total;
        this.precioTotal = total; // sin impuestos/descuentos adicionales en este ejercicio
    }

    /** Metodo de dominio del diagrama: transiciona el estado (ej. anular una orden ya emitida). */
    public void cambiarEstado(EstadoOrden nuevoEstado) {
        this.estado = nuevoEstado;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleOrden> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrden> detalles) {
        this.detalles = detalles;
    }
}
