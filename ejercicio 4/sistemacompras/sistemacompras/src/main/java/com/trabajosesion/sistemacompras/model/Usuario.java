package com.trabajosesion.sistemacompras.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL (capa Model de MVC) - Entidad JPA "Usuario".
 *
 * @Entity      : le indica a Hibernate que esta clase representa una tabla
 *                de la base de datos. Cada instancia = una fila.
 * @Table       : fija explicitamente el nombre de la tabla ("usuarios").
 *                Si se omite, Hibernate usaria el nombre de la clase, pero
 *                se prefiere ser explicito por claridad.
 *
 * Usuario extiende Persona (herencia UML del diagrama). Como Persona esta
 * anotada con @MappedSuperclass, todas las columnas de Persona (nombre,
 * apellido, documento, correo, password, intentosFallidos, estado) pasan a
 * formar parte de la tabla "usuarios" ademas del id propio de Usuario.
 *
 * Relacion con Compra (ASOCIACION 1-a-muchos del diagrama):
 * un Usuario puede realizar 0 o muchas Compras a lo largo del tiempo, y
 * cada Compra pertenece a exactamente un Usuario. El lado "dueño" de la
 * relacion (el que tiene la columna de clave foranea) es Compra, por eso
 * aca se usa mappedBy: le decimos a Hibernate "esta relacion ya esta
 * mapeada del lado de Compra, en su atributo 'usuario'".
 */
@Entity
@Table(name = "usuarios")
public class Usuario extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Lado inverso de la asociacion Usuario (1) --- (0..*) Compra.
     * FetchType.LAZY: las compras de un usuario NO se traen automaticamente
     * de la base cada vez que se carga un Usuario; solo se consultan si el
     * codigo realmente accede a getCompras(). Esto evita cargar datos que
     * no siempre se necesitan (por ejemplo, al hacer login no hace falta
     * traer todo el historial de compras).
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Compra> compras = new ArrayList<>();

    public Usuario() {
        super();
    }

    public Usuario(String nombre, String apellido, String documento,
                    LocalDate fechaDeNacimiento, String correo, String password) {
        super(nombre, apellido, documento, fechaDeNacimiento, correo, password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
}
