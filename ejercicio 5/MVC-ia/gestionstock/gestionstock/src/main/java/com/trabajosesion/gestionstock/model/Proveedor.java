package com.trabajosesion.gestionstock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Proveedor".
 *
 * ADAPTACION RESPECTO DEL DIAGRAMA: se agregan "razonSocial", "cuit" y
 * "rubro" (no estaban en el diagrama original). El prototipo de interfaz
 * muestra estas tres columnas en la pantalla "Proveedores" y en sus
 * formularios de alta/edicion; sin ellas, esa pantalla no tendria datos
 * reales que mostrar. Ademas es informacion minima e imprescindible para
 * identificar a un proveedor mayorista en un sistema real.
 *
 * Tambien se cambia "telefono" de int a String (un telefono puede llevar
 * "+", espacios o un 0 inicial que un int no puede representar), y se
 * corrige el nombre del atributo booleano de baja logica: el diagrama
 * original lo llamaba "eliminador" (probable error de tipeo del
 * enunciado); se deja como "eliminado", consistente con el resto de las
 * entidades del modelo.
 */
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    @Column(name = "razon_social", nullable = false, length = 120)
    private String razonSocial;

    @Column(nullable = false, unique = true, length = 20)
    private String cuit;

    @Column(length = 60)
    private String rubro;

    @Column(length = 30)
    private String telefono;

    @Column(length = 150)
    private String direccion;

    @Column(length = 120)
    private String correo;

    @Column(nullable = false)
    private boolean eliminado = false;

    /** ASOCIACION (1 Proveedor --- 0..* OrdenCompra), lado inverso. */
    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<OrdenCompra> ordenes = new ArrayList<>();

    public Proveedor() {
    }

    public Proveedor(String razonSocial, String cuit, String rubro, String telefono, String direccion, String correo) {
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.rubro = rubro;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public List<OrdenCompra> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenCompra> ordenes) {
        this.ordenes = ordenes;
    }
}
