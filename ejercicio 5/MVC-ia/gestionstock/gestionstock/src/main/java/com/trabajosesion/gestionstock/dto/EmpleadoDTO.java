package com.trabajosesion.gestionstock.dto;

/**
 * DTO de Empleado. Se usa SOLO de lectura (para que el DataInitializer y
 * futuras pantallas puedan mostrar informacion de un empleado sin exponer
 * jamas el hash de la contrasena hacia una vista). Notese que a
 * diferencia de la entidad Empleado, este DTO NO tiene el campo
 * "password": es una decision deliberada, no un olvido - ninguna vista de
 * este sistema necesita (ni deberia poder) mostrar la contrasena de
 * alguien, ni siquiera hasheada.
 */
public class EmpleadoDTO {

    private Long idEmpleado;
    private String nombre;
    private String correo;
    private String cargo;

    public EmpleadoDTO() {
    }

    public EmpleadoDTO(Long idEmpleado, String nombre, String correo, String cargo) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.correo = correo;
        this.cargo = cargo;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
