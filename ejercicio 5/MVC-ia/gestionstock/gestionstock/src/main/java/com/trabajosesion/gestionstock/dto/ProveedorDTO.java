package com.trabajosesion.gestionstock.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de Proveedor. Igual justificacion que ProductoDTO: transporta datos
 * planos entre Controller <-> Service <-> Vista, sin exponer la relacion
 * @OneToMany hacia OrdenCompra que tiene la entidad Proveedor (esa lista
 * de ordenes no le interesa a un formulario de alta/edicion de proveedor).
 */
public class ProveedorDTO {

    private Long idProveedor;

    @NotBlank(message = "La razon social es obligatoria")
    private String razonSocial;

    @NotBlank(message = "El CUIT es obligatorio")
    private String cuit;

    private String rubro;

    private String telefono;

    private String direccion;

    private String correo;

    public ProveedorDTO() {
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
}
