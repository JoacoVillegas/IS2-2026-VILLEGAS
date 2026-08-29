package com.trabajosesion.sistemacompras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Administrador".
 *
 * Igual que Usuario, hereda de Persona (via @MappedSuperclass) por lo que
 * comparte todas las columnas de autenticacion y datos personales, pero
 * tiene su propia tabla ("administradores") y su atributo especifico
 * "nivelAcceso" tal como indica el diagrama de clases.
 *
 * No tiene relacion directa con Compra: en este modelo, quien compra es
 * siempre un Usuario. El Administrador gestiona el sistema (usuarios,
 * productos) pero no genera compras.
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nivel_acceso", length = 30)
    private String nivelAcceso;

    public Administrador() {
        super();
    }

    public Administrador(String nombre, String apellido, String documento,
                          LocalDate fechaDeNacimiento, String correo, String password,
                          String nivelAcceso) {
        super(nombre, apellido, documento, fechaDeNacimiento, correo, password);
        this.nivelAcceso = nivelAcceso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
}
