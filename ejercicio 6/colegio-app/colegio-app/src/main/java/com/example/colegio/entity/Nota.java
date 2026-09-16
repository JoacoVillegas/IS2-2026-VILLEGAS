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
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * INCONSISTENCIA DETECTADA EN EL DIAGRAMA (ver analisis inicial en el chat /
 * README.md, seccion "Analisis del diagrama"):
 *
 * En el diagrama original, "Nota" solo esta conectada a "Materia"
 * (Materia 1 -- * Nota). Esto es insuficiente: una nota necesariamente
 * pertenece a un ALUMNO ademas de a una materia (el enunciado dice
 * literalmente "se registran las notas por materias de LOS ALUMNOS"). Sin
 * una referencia al alumno, seria imposible saber de quien es cada nota.
 *
 * Por eso se agrega aqui la relacion Nota -> Alumno (ManyToOne) que NO
 * figuraba explicitamente en el diagrama, dejando constancia de que es una
 * correccion necesaria y no una decision estetica.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nota")
@Audited
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNota;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private float valor;

    /** Relacion presente en el diagrama original. */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    /**
     * Relacion AGREGADA para corregir la inconsistencia explicada arriba.
     * Sin esta columna la entidad Nota no tendria sentido de negocio.
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;
}
