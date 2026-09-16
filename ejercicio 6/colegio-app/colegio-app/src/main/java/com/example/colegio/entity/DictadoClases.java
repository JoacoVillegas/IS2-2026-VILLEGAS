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
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.NotAudited;

/**
 * "DictadoClases" es la clase de asociacion del diagrama original que
 * conecta Materia con Profesor/Alumno ("+asignarProfesor()",
 * "+listarAlumnos()"). Se interpreta como: "un Profesor dicta una Materia en
 * un Aula/Curso durante un anioLectivo determinado".
 *
 * Esta entidad es la que permite resolver el requisito de autorizacion mas
 * fino del enunciado (punto 6): "el profesor podra consultar informacion de
 * los alumnos de los cursos/materias que tenga a cargo". En lugar de que un
 * profesor pueda ver TODOS los alumnos o TODAS las materias del colegio,
 * ProfesorService filtra usando esta tabla de asignaciones.
 *
 * DECISION DE DISENO ADICIONAL: no se marca con @Audited. Se trata de una
 * tabla de asignacion/planificacion (que docente dicta que materia en que
 * curso), que se recalcula año a año; auditar su historial no forma parte de
 * los requisitos de auditoria del enunciado (que pide auditar Profesor,
 * Alumno, Materia, Curso y Nota explicitamente).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dictado_clases")
public class DictadoClases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDictado;

    /** Ej: LocalDate.of(2026, 3, 1) representando el ciclo lectivo 2026. */
    @Column(nullable = false)
    private LocalDate anioLectivo;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;
}
