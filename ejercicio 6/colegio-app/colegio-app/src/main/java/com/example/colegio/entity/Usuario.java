package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DECISION DE DISENO ADICIONAL: entidad "Usuario" separada del dominio academico.
 *
 * El enunciado pide que "los docentes ingresan con usuario y contraseña" y que
 * "el usuario es el correo personal del docente". El diagrama de clases
 * original NO contiene ninguna entidad de autenticacion (es logico: fue
 * diseñado antes de agregar el requisito de seguridad).
 *
 * En lugar de agregar los campos "correo" y "password" directamente dentro de
 * la clase Profesor, se los separa en esta entidad "Usuario" por dos motivos:
 *   1. Principio de responsabilidad unica: Profesor representa un docente del
 *      colegio (sus datos academicos); Usuario representa una identidad de
 *      acceso al sistema (credenciales). Son dos preocupaciones distintas.
 *   2. Permite que el ADMIN tambien exista como fila de la tabla "usuario"
 *      SIN necesidad de crear una entidad "Profesor" ficticia para el
 *      administrador (el admin no es un docente del colegio).
 *
 * Relacion con Profesor: Usuario 1 <-> 1 Profesor (OneToOne), obligatoria
 * solo para usuarios con rol PROFESOR. El usuario ADMIN no tiene Profesor
 * asociado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username de login = correo personal del docente (o del admin).
     * @Column(unique = true) genera una restriccion UNIQUE a nivel de base de
     * datos: MySQL rechazara cualquier INSERT/UPDATE que intente repetir un
     * correo ya existente.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Password ya cifrado con BCrypt (nunca texto plano). Se llama
     * "passwordHash" y no "password" para dejar explicito, incluso a nivel de
     * nombre de variable, que jamas contiene texto plano.
     */
    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean habilitado = true;

    // NOTA SOBRE AUDITORIA: esta entidad deliberadamente NO se marca @Audited
    // (ver Profesor.java, Alumno.java, Materia.java, Aula.java y Nota.java
    // para los ejemplos con @Audited). Se justifica en el README, seccion
    // "Que se audita y que no", pero en resumen: auditar hashes de
    // contraseña en una tabla historica (usuario_AUD) duplicaria datos
    // sensibles sin aportar valor academico, y el cambio de contraseña ya
    // quiere registrarse mediante logs de aplicacion, no mediante Envers.
}
