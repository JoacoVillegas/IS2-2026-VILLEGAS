package com.trabajosesion.sistemacompras.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Compra".
 *
 * ASOCIACION: Compra (muchas) --- (1) Usuario. Esta clase es el lado
 * "dueño" de esa relacion: tiene la columna de clave foranea "usuario_id".
 *
 * COMPOSICION: Compra (1) *--- (1..*) DetalleCompra. Se modela con
 * cascade = CascadeType.ALL + orphanRemoval = true: si se borra una
 * Compra, Hibernate borra en cascada todos sus DetalleCompra (porque un
 * detalle de compra no tiene sentido de existir sin la compra a la que
 * pertenece); y si un DetalleCompra se quita de la lista "detalles" sin
 * borrar la Compra, tambien se elimina de la base (orphanRemoval).
 *
 * ADAPTACION RESPECTO DEL DIAGRAMA: se agrega el atributo booleano
 * "anulada" (no estaba en el diagrama original) porque el metodo
 * anularCompra() del diagrama necesita algun estado persistente sobre el
 * cual operar; sin este campo, "anular" una compra no tendria ningun
 * efecto observable ni en el modelo ni en la base de datos.
 */
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean anulada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleCompra> detalles = new ArrayList<>();

    public Compra() {
    }

    public Compra(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Metodo de dominio (estaba en el diagrama original). Marca el
     * instante en que la compra se registra formalmente. La persistencia
     * real (INSERT en la base) la realiza siempre el Service a traves del
     * DAO/Repository; este metodo solo prepara el estado del objeto.
     */
    public void registrarCompra() {
        this.fechaCompra = LocalDateTime.now();
    }

    /**
     * Agrega un detalle a la compra (relacion de composicion) y mantiene
     * sincronizado el precioTotal, recorriendo los subtotales de cada
     * detalle ya cargado.
     */
    public void agregarDetalle(DetalleCompra detalle) {
        detalle.setCompra(this);
        this.detalles.add(detalle);
        recalcularTotal();
    }

    public void anularCompra() {
        this.anulada = true;
    }

    private void recalcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCompra d : this.detalles) {
            if (d.getSubtotal() != null) {
                total = total.add(d.getSubtotal());
            }
        }
        this.precioTotal = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public boolean isAnulada() {
        return anulada;
    }

    public void setAnulada(boolean anulada) {
        this.anulada = anulada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }
}
