package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * CLASE BASE (no es una entidad JPA por si misma, ver @MappedSuperclass).
 *
 * DECISION DE DISENO ADICIONAL:
 * El diagrama de clases original modela "Persona" como una superclase de la
 * cual heredan "Profesor" y "Alumno" (relaciones "Extends"). JPA ofrece varias
 * estrategias para mapear herencia (SINGLE_TABLE, JOINED, TABLE_PER_CLASS) o,
 * alternativamente, @MappedSuperclass.
 *
 * Se eligio @MappedSuperclass en lugar de una jerarquia @Entity con @Inheritance
 * porque:
 *   1. Profesor y Alumno NO se consultan nunca de forma polimorfica como una
 *      unica lista de "Personas" en los requerimientos (siempre se piden por
 *      separado: listado de profesores, listado de alumnos).
 *   2. Hibernate Envers audita cada tabla fisica de forma independiente; con
 *      @MappedSuperclass cada subclase tiene su propia tabla y su propia
 *      tabla de auditoria (profesor_AUD, alumno_AUD), lo cual es mas simple
 *      de explicar y de leer que una jerarquia JOINED con tablas cruzadas.
 * Esta es una decision de diseno tecnica, no un requerimiento del enunciado.
 *
 * @MappedSuperclass: le indica a JPA que esta clase NO tiene tabla propia,
 * pero que sus campos (@Id incluido) se "copian" dentro de la tabla de cada
 * subclase que la extienda (Profesor, Alumno).
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class Persona {

    /**
     * @Id marca el campo como clave primaria de la tabla.
     * @GeneratedValue(strategy = IDENTITY) delega en la base de datos (MySQL)
     * la generacion del valor mediante una columna AUTO_INCREMENT.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 100)
    protected String nombre;

    @Column(nullable = false, length = 100)
    protected String apellido;

    /**
     * Baja logica: en lugar de eliminar fisicamente el registro de la base de
     * datos (DELETE), se marca "eliminado = true" y se lo excluye de las
     * consultas normales. Ver README.md / seccion de decisiones de diseno
     * para la justificacion completa de por que se usa baja logica en todo
     * el dominio academico (Profesor, Alumno, Materia, Aula, Grado).
     */
    @Column(nullable = false)
    protected boolean eliminado = false;
}
