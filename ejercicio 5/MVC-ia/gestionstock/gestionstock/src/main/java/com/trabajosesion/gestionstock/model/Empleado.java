package com.trabajosesion.gestionstock.model;

import jakarta.persistence.CascadeType;
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
 * MODEL (capa Model de MVC) - Entidad JPA "Empleado".
 *
 * @Entity + @Table: le indican a Hibernate que esta clase representa la
 * tabla "empleados" (una fila = un Empleado).
 *
 * ADAPTACION RESPECTO DEL DIAGRAMA (justificada en el punto 9 del
 * enunciado, que agrega el requisito de login):
 *   El diagrama original NO tenia un atributo "password". El enunciado
 *   pide agregar acceso mediante usuario y contrasena, y sugiere crear una
 *   entidad Usuario "si es necesario". Se decidio NO crear una entidad
 *   Usuario separada porque seria redundante: el prototipo de interfaz
 *   muestra literalmente "Vista de Empleado" apenas se inicia sesion, y
 *   Empleado ya tiene un atributo "correo" que cumple perfectamente el rol
 *   de nombre de usuario. Por eso se agrega directamente "password" aca.
 *   El correo se marca @Column(unique = true) porque es la clave de login.
 *
 * La contrasena NUNCA se guarda en texto plano: EmpleadoServiceImpl la
 * hashea con BCrypt (ver PasswordEncoderConfig) antes de persistirla.
 *
 * "eliminado" implementa BAJA LOGICA (soft delete): eliminarEmpleado() no
 * borra la fila de la base, solo marca este flag en true. Esto preserva
 * el historial (por ejemplo, las OrdenCompra ya registradas por ese
 * empleado siguen teniendo una referencia valida) y es exactamente para
 * lo que el diagrama de clases ya preveia este atributo booleano.
 */
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @Column(nullable = false, length = 80)
    private String nombre;

    // int -> String: un DNI puede tener ceros a la izquierda; ademas,
    // en Argentina el CUIL/DNI se maneja habitualmente como texto.
    @Column(nullable = false, length = 20)
    private String dni;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    // Hash BCrypt de la contrasena (nunca texto plano). BCrypt genera
    // hashes de 60 caracteres, pero se deja margen (length=100) por si en
    // el futuro se cambia el algoritmo de hashing.
    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 60)
    private String cargo;

    @Column(nullable = false)
    private boolean eliminado = false;

    /**
     * ASOCIACION (1 Empleado --- 0..* OrdenCompra): un empleado puede
     * registrar muchas ordenes de compra a lo largo del tiempo. Es el
     * lado inverso (mappedBy): la clave foranea "empleado_id" vive en la
     * tabla "ordenes_compra".
     */
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenCompra> ordenes = new ArrayList<>();

    public Empleado() {
    }

    public Empleado(String nombre, String dni, String correo, String password, String cargo) {
        this.nombre = nombre;
        this.dni = dni;
        this.correo = correo;
        this.password = password;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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
