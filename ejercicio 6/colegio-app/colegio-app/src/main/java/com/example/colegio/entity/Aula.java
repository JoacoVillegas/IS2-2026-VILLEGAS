package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * "Aula" es la clase del diagrama que se utiliza para representar el
 * concepto de "Curso" pedido en los nuevos requerimientos (punto 5 y 13 del
 * enunciado: ABM de "Cursos").
 *
 * DECISION DE DISENO ADICIONAL (justificacion de mapeo Curso -> Aula):
 * El enunciado nuevo pide administrar "Cursos", pero el diagrama de clases
 * provisto no tiene una clase llamada literalmente "Curso": tiene "Grado"
 * (el nivel, ej. "3er grado") y "Aula" (la division dentro de ese nivel, ej.
 * "3ro A", con su cantidad de alumnos). En la jerga escolar de Argentina,
 * lo que coloquialmente se llama "curso" (ej. "3ro A") corresponde
 * exactamente a la combinacion Grado + Aula del diagrama, y es la entidad de
 * la que un Alumno es miembro directo (Aula 1..* Alumno). Por eso el ABM de
 * "Curso" pedido en el enunciado se implementa administrando esta entidad
 * "Aula" (que ya referencia a su Grado). No se renombro la clase para
 * mantener trazabilidad con el diagrama original, pero en las vistas
 * Thymeleaf y en las URLs (/admin/cursos) se la presenta al usuario final
 * como "Curso", que es el termino que pide el enunciado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aula")
@Audited
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAula;

    /** Ej: "A", "B". Combinado con el nivel del Grado forma "3ro A". */
    @Column(nullable = false, length = 20)
    private String division;

    @Column(nullable = false)
    private boolean eliminado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado_id", nullable = false)
    private Grado grado;

    /**
     * Metodo de dominio presente en el diagrama original
     * ("+obtenerCantidadAlumnos(): int"). Se mantiene como comportamiento de
     * la entidad porque es un calculo derivado de sus propios datos
     * (coleccion de alumnos), no logica de negocio de otra capa.
     * NOTA: no se mapea la coleccion de Alumnos en sentido inverso
     * (@OneToMany) desde Aula para evitar colecciones perezosas pesadas en
     * cada pantalla de curso; en su lugar, AlumnoService expone
     * contarPorAula(aulaId) usando una consulta derivada del repositorio,
     * y este metodo de conveniencia delega en el resultado que le pasan
     * (ver AulaServiceImpl).
     */
    public int obtenerCantidadAlumnos(int cantidadCalculadaPorService) {
        return cantidadCalculadaPorService;
    }
}
