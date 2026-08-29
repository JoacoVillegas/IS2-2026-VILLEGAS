package com.trabajosesion.sistemacompras.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

/**
 * MODEL (capa Model de MVC) - Clase base abstracta del dominio.
 *
 * Corresponde a la clase "Persona" del diagrama de clases corregido.
 * Concentra los atributos y el comportamiento de autenticacion que
 * comparten TODAS las personas que interactuan con el sistema (Usuario y
 * Administrador), evitando que cada subclase repita el mismo codigo
 * (principio de diseño DRY aplicado en la correccion del diagrama).
 *
 * ADAPTACION RESPECTO DEL DIAGRAMA UML (se explica tal como pide el
 * enunciado en el punto 7):
 *   En el diagrama, "Persona" es una clase abstracta de la que "Usuario" y
 *   "Administrador" heredan mediante herencia UML clasica. Si mapeamos esa
 *   herencia 1 a 1 con JPA usando @Entity + @Inheritance en Persona,
 *   Hibernate necesitaria crear una estrategia de tablas (JOINED,
 *   SINGLE_TABLE o TABLE_PER_CLASS) con una tabla "personas" real, lo cual
 *   agrega una complejidad de mapeo que no aporta nada didactico extra a
 *   este ejercicio.
 *   En su lugar se usa @MappedSuperclass: esto le dice a JPA/Hibernate que
 *   Persona NO es una entidad ni tiene tabla propia en la base de datos;
 *   simplemente aporta sus columnas (@Column) a las tablas de las clases
 *   que SI son @Entity (Usuario y Administrador). El resultado final es el
 *   mismo desde el punto de vista del modelo de objetos (Usuario y
 *   Administrador siguen heredando de Persona en Java), pero en la base de
 *   datos cada uno tiene su propia tabla independiente con las columnas de
 *   Persona ya incluidas. Es la forma mas simple y mas usada en la practica
 *   para este tipo de herencia "de conveniencia" entre entidades que en
 *   realidad no comparten identidad ni consultas polimorficas.
 */
@MappedSuperclass
public abstract class Persona {

    // --- Datos personales (tal como los pide el enunciado original) ---

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    // Se cambia de int a String respecto del diagrama original: un
    // documento de identidad puede tener ceros a la izquierda o (segun el
    // pais) letras, por lo que un tipo numerico puede perder informacion.
    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaDeNacimiento;

    // El enunciado indica textualmente: "Se utiliza como usuario del
    // sistema el correo personal". Por eso el correo es unique: es la
    // clave de login de la persona (equivalente a un "username").
    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    // --- Datos de acceso al sistema (subidos desde Usuario/Administrador
    //     para no duplicarlos, ver comentario de la clase) ---

    @Column(nullable = false)
    private String password;

    @Column(name = "intentos_fallidos", nullable = false)
    private int intentosFallidos = 0;

    // @Enumerated(EnumType.STRING) le indica a Hibernate que en la base de
    // datos esta columna se guarde como texto ("ACTIVO"/"BLOQUEADO") y no
    // como el numero de posicion del enum (0/1), que seria ilegible y
    // fragil ante cambios futuros en el enum.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCuenta estado = EstadoCuenta.ACTIVO;

    protected Persona() {
        // Constructor vacio requerido por JPA/Hibernate: el proveedor de
        // persistencia necesita poder instanciar la entidad por reflexion
        // antes de rellenarla con los datos leidos de la base.
    }

    protected Persona(String nombre, String apellido, String documento,
                       LocalDate fechaDeNacimiento, String correo, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.correo = correo;
        this.password = password;
        this.estado = EstadoCuenta.ACTIVO;
        this.intentosFallidos = 0;
    }

    /**
     * Metodo de dominio (estaba en el diagrama original de Persona).
     * Es una operacion puramente de lectura/formato: no accede a la base
     * de datos, solo compone dos atributos ya cargados en memoria.
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    /**
     * Verifica si la contrasena recibida coincide con la almacenada y si
     * la cuenta esta en condiciones de iniciar sesion (no bloqueada).
     *
     * IMPORTANTE: este metodo SOLO valida datos ya cargados en memoria.
     * No persiste ningun cambio en la base de datos: quien orquesta que
     * ese cambio (sumar intento fallido, bloquear, guardar) se persista es
     * siempre el Service, llamando despues al DAO/Repository. Esto respeta
     * la separacion de capas: el Model concentra la regla de negocio "que"
     * hay que hacer, y el Service decide "cuando" guardarlo.
     */
    public boolean iniciarSesion(String passwordIngresada) {
        if (this.estado == EstadoCuenta.BLOQUEADO) {
            return false;
        }
        return this.password.equals(passwordIngresada);
    }

    /** Suma un intento fallido y bloquea la cuenta automaticamente al llegar a 3. */
    public void sumarIntentoFallido() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= 3) {
            bloquear();
        }
    }

    public void bloquear() {
        this.estado = EstadoCuenta.BLOQUEADO;
    }

    public void resetearIntentos() {
        this.intentosFallidos = 0;
        this.estado = EstadoCuenta.ACTIVO;
    }

    // ------------------- Getters y Setters -------------------

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
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

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }
}
