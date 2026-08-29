package com.trabajosesion.sistemacompras.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Producto".
 *
 * Se agrega el atributo "precio" respecto del diagrama original: un
 * producto vendible necesita un precio de referencia para poder calcular
 * el precioUnitario/subtotal de un DetalleCompra. Es una adaptacion minima
 * y necesaria para que la funcionalidad de compras sea real (punto 7 y 11
 * del enunciado: la implementacion debe ser funcional, no una maqueta).
 *
 * Relacion con Inventario (AGREGACION del diagrama, 1 Producto --- 0..*
 * Inventario): un producto puede tener muchos movimientos de stock a lo
 * largo del tiempo (altas, bajas por venta, ajustes). Se modela como
 * agregacion y no como composicion porque, conceptualmente, los registros
 * historicos de inventario tienen valor por si mismos (auditoria) aunque
 * el producto se diera de baja; por eso NO se usa
 * cascade = CascadeType.REMOVE aca: borrar un Producto no debe arrastrar
 * el borrado de su historial de movimientos.
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /**
     * Lado inverso de la agregacion. cascade = PERSIST/MERGE (sin REMOVE):
     * si guardo un Producto junto con nuevos objetos Inventario ya
     * asociados en memoria, Hibernate los persiste en cascada; pero borrar
     * el producto NO borra en cascada su historial (agregacion, no
     * composicion).
     */
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Inventario> movimientosInventario = new ArrayList<>();

    public Producto() {
    }

    public Producto(String nombre, String descripcion, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public List<Inventario> getMovimientosInventario() {
        return movimientosInventario;
    }

    public void setMovimientosInventario(List<Inventario> movimientosInventario) {
        this.movimientosInventario = movimientosInventario;
    }
}
