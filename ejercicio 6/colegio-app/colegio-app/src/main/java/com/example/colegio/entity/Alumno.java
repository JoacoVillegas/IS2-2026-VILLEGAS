package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * Entidad Alumno. Extiende Persona (id, nombre, apellido, eliminado).
 *
 * El diagrama de clases original ya incluye "fechaNacimiento" como atributo
 * PROPIO de Alumno (no heredado de Persona), asi que se respeta tal cual:
 * ver el diagrama, la caja "Alumno" tiene su propio campo fechaNacimiento
 * ademas de heredar de Persona.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "alumno")
@Audited
public class Alumno extends Persona {

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    /**
     * Segun el diagrama: Aula (1) -- (1..*) Alumno. Cada alumno pertenece a
     * un unico Aula/Curso.
     *
     * @NotAudited en la relacion: se decidio auditar los DATOS del alumno
     * (nombre, apellido, fecha de nacimiento, baja logica) pero no cada
     * cambio de curso/aula, ya que un cambio de aula es una operacion
     * administrativa frecuente (promocion de año, cambio de division) que
     * generaria muchisimo ruido en la tabla alumno_AUD sin valor academico
     * relevante para este ejercicio. Es una decision de diseno adicional,
     * documentada aqui para que sea explicita y discutible.
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;
}
