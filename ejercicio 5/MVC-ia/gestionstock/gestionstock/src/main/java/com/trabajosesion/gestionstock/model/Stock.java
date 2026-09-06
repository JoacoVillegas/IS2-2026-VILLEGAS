package com.trabajosesion.gestionstock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Stock".
 *
 * Lado "dueño" de la agregacion 1-a-1 con Producto: esta clase tiene la
 * columna de clave foranea "producto_id", marcada @Column(unique = true)
 * a traves de @JoinColumn para que la base de datos garantice que nunca
 * haya dos registros de Stock apuntando al mismo Producto (refuerza la
 * cardinalidad 1...1 del diagrama a nivel de base de datos, no solo en
 * el codigo Java).
 *
 * "idMovimiento", "fecha" y "tipoMovimiento" reflejan el ULTIMO
 * movimiento aplicado (alta inicial, incremento por compra, etc.);
 * "stockActual" es la cantidad vigente. incrementarStock()/
 * decrementarStock() actualizan ambos datos juntos, manteniendo la
 * entidad siempre consistente con su propio historial reciente.
 */
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "tipo_movimiento", length = 40)
    private String tipoMovimiento;

    @Column(name = "stock_actual", nullable = false)
    private int stockActual;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;

    public Stock() {
    }

    public Stock(int stockActual, String tipoMovimiento, Producto producto) {
        this.stockActual = stockActual;
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = LocalDateTime.now();
        this.producto = producto;
    }

    /** Metodo de dominio del diagrama: suma unidades (ej: llega mercaderia de un proveedor). */
    public void incrementarStock(int cantidad, String motivo) {
        this.stockActual += cantidad;
        this.tipoMovimiento = motivo;
        this.fecha = LocalDateTime.now();
    }

    /** Metodo de dominio del diagrama: resta unidades (se deja disponible para usos futuros, p. ej. ventas). */
    public void decrementarStock(int cantidad, String motivo) {
        this.stockActual -= cantidad;
        this.tipoMovimiento = motivo;
        this.fecha = LocalDateTime.now();
    }

    /** Metodo de dominio del diagrama: consulta de solo lectura de la cantidad vigente. */
    public int consultarStock() {
        return this.stockActual;
    }

    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
