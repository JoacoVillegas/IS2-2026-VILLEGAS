package com.trabajosesion.gestionstock.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Producto".
 *
 * ADAPTACION RESPECTO DEL DIAGRAMA: se agrega el atributo "precio". El
 * diagrama original no lo incluye, pero el prototipo de interfaz muestra
 * una columna "Precio" en el listado de Inventario y un campo "Precio" en
 * los formularios de alta/edicion de producto: sin este atributo, esas
 * pantallas no tendrian un dato real que mostrar/cargar.
 *
 * AGREGACION (1 Producto --- 1 Stock): el diagrama de clases dibuja el
 * rombo (agregacion) del lado de Producto con cardinalidad "1...1" hacia
 * Stock. Esto se interpreta como: cada producto tiene EXACTAMENTE UN
 * registro de Stock asociado (que se actualiza in-place con cada
 * movimiento), y no un historial de movimientos independientes. Por eso
 * se mapea como @OneToOne y no como @OneToMany.
 * cascade = CascadeType.ALL: al guardar un Producto nuevo junto con su
 * Stock inicial (ver ProductoServiceImpl), Hibernate persiste ambos en la
 * misma operacion; y si un producto se elimina fisicamente (no es el caso
 * aca, que usa baja logica, pero queda correcto igual), su stock asociado
 * se elimina con el (tiene sentido: un registro de stock sin producto no
 * significa nada).
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

    @Column(length = 60)
    private String marca;

    @Column(length = 60)
    private String categoria;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean eliminado = false;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Stock stock;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, String marca, String categoria, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.categoria = categoria;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
