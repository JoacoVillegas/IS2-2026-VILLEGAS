package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * Entidad que representa la institucion. Se mantiene del diagrama original
 * (Colegio 1 -- * Grado).
 *
 * DECISION DE DISENO ADICIONAL: el enunciado de ABM (punto 13) solo pide
 * ABM explicito de Profesores, Materias, Cursos y Alumnos; no menciona ABM
 * de "Colegio". Como el sistema modela un unico colegio (no es multi-tenant),
 * se conserva la entidad para respetar el diagrama y las relaciones de
 * cardinalidad hacia Grado, pero NO se expone un CRUD web para ella: se crea
 * un unico registro mediante datos de prueba (data.sql) al iniciar la
 * aplicacion. Si en el futuro se necesitara administrar varios colegios,
 * alcanza con agregar un ColegioController siguiendo el mismo patron que
 * ProfesorController.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "colegio")
@Audited
public class Colegio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColegio;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false)
    private boolean eliminado = false;
}
