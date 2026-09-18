package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "actividad")
@Audited
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private long cupos;

    @Column(nullable = false)
    private boolean eliminado = false;

    // Los metodos "inscribirSocio()"/"cancelarInscripcion()"/"consultarCupos()"
    // del diagrama original NO se implementan como metodos de esta entidad:
    // requieren consultar/crear filas de Inscripcion (otra tabla), lo cual
    // es responsabilidad de la capa de negocio (InscripcionService), no de
    // la entidad JPA en si (una entidad no deberia depender de un
    // Repository para calcular su propio estado). Ver InscripcionServiceImpl.
}
