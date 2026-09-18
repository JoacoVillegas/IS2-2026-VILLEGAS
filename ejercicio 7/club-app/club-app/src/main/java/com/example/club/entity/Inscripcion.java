package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * CORRECCION DE INCONSISTENCIA DEL DIAGRAMA: la relacion "Socio *...*
 * Actividad" estaba dibujada con una flecha de herencia (punta hueca), lo
 * cual es un error de notacion (Actividad no es una subclase de Socio). Se
 * corrige modelando una relacion muchos-a-muchos real, materializada como
 * esta entidad de asociacion explicita (en lugar de un simple
 * @ManyToMany), porque se necesitan atributos propios de la relacion
 * (fechaInscripcion, estado) para poder distinguir una inscripcion vigente
 * de una cancelada -- algo que Actividad.cupos y los metodos
 * inscribirSocio()/cancelarInscripcion() del diagrama exigen poder hacer.
 *
 * DECISION DE DISENO: no se audita (@Audited ausente), mismo criterio que
 * DictadoClases en el proyecto de colegio: es una tabla de estado
 * operativo (quien esta anotado a que actividad), no un dato "critico" que
 * requiera historial para este ejercicio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoInscripcion estado;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;
}
