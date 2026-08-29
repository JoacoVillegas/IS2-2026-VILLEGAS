package com.trabajosesion.sistemacompras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Inventario".
 *
 * Representa un MOVIMIENTO de stock de un producto (no "el stock total" en
 * si mismo, sino cada registro individual de entrada o salida). El stock
 * actual de un producto se puede calcular en cualquier momento sumando la
 * columna "cantidad" de todos sus movimientos (positivo = entrada de
 * mercaderia, negativo = salida por venta).
 *
 * Lado "dueño" de la agregacion Producto (1) --- (0..*) Inventario: esta
 * clase tiene la clave foranea "producto_id" (@JoinColumn), por eso es
 * quien declara @ManyToOne. FetchType.LAZY evita traer el producto
 * completo cada vez que se lista el historial de movimientos, salvo que el
 * codigo lo pida explicitamente.
 */
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // Cantidad del movimiento: positiva = ingreso de stock,
    // negativa = egreso de stock (por ejemplo, al confirmar una compra).
    @Column(nullable = false)
    private int cantidad;

    @Column(length = 40)
    private String tipoMovimiento; // "ALTA_INICIAL", "VENTA", "AJUSTE", etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Inventario() {
    }

    public Inventario(LocalDateTime fecha, int cantidad, String tipoMovimiento, Producto producto) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
