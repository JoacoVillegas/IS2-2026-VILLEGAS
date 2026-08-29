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

import java.math.BigDecimal;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "DetalleCompra".
 *
 * COMPOSICION: lado "parte" de la relacion con Compra (no puede existir sin
 * una Compra due&ntilde;a). Tiene la clave foranea "compra_id".
 *
 * ASOCIACION (agregada en la correccion del diagrama): DetalleCompra
 * (muchos) --- (1) Producto. Es la relacion que faltaba en el diagrama
 * original: sin ella, un detalle de compra no tenia forma de saber QUE
 * producto se estaba comprando, solo se vinculaba directamente con
 * Inventario. Ahora el flujo correcto es:
 *   DetalleCompra conoce su Producto y su cantidad/precioUnitario
 *   -> al confirmarse la compra, se genera un nuevo registro en Inventario
 *      (cantidad negativa) para descontar el stock de ese Producto.
 *
 * Se agregan los atributos "cantidad" y "precioUnitario" (no estaban en el
 * diagrama original) porque son imprescindibles para poder calcular el
 * subtotal de cada linea de compra; sin ellos, DetalleCompra no tendria
 * ningun dato real que registrar.
 */
@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public DetalleCompra() {
    }

    public DetalleCompra(Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * Metodo de dominio del diagrama original. La logica de "bajar" stock
     * en si misma (crear el registro de Inventario correspondiente) se
     * delega al Service (CompraServiceImpl), porque requiere coordinar dos
     * entidades distintas (Producto/Inventario) y decidir persistencia:
     * responsabilidad tipica de la capa Service, no del Model ni del DAO.
     * Este metodo se deja documentado aqui porque conceptualmente
     * pertenece al detalle de compra segun el diagrama, aunque su
     * implementacion efectiva viva en CompraServiceImpl.disminuirStock(...).
     */
    public int getCantidadADescontar() {
        return -this.cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
