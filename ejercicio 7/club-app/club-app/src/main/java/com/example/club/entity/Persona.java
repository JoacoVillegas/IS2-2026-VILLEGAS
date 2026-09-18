package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.NotAudited;

/**
 * Persona: a diferencia del proyecto de colegio (donde "Persona" era solo
 * una superclase auxiliar, nunca consultada de forma independiente), en
 * este dominio SI necesitamos poder guardar y consultar Personas "puras"
 * (integrantes de un grupo familiar que no son socios, ej. hijos menores o
 * el conyuge no titular). Por eso la estrategia correcta aqui NO es
 * @MappedSuperclass (que no genera tabla propia), sino una jerarquia JPA
 * real con @Inheritance:
 *
 * DECISION DE DISENO: se usa InheritanceType.JOINED. Esto crea una tabla
 * "persona" con las columnas comunes, y una tabla "socio" con SOLO las
 * columnas adicionales de Socio (fechaAlta, estado) mas una FK que es a la
 * vez PK hacia "persona" (id compartido). Se prefirio JOINED sobre
 * SINGLE_TABLE porque evita una tabla unica con columnas nulleables para
 * los atributos exclusivos de Socio, y refleja mejor en la base de datos
 * que "todo Socio es una Persona, pero no toda Persona es Socio".
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "persona")
@jakarta.persistence.Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
@org.hibernate.envers.Audited
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 100)
    protected String nombre;

    @Column(nullable = false, length = 100)
    protected String apellido;

    @Column(nullable = false)
    protected LocalDate fechaNacimiento;

    @Column(nullable = false)
    protected boolean eliminado = false;

    /**
     * CORRECCION DE INCONSISTENCIA DEL DIAGRAMA: el diagrama original dibuja
     * "Persona *...1 Imagen" (muchas Personas a una Imagen), lo cual no
     * tiene sentido de negocio (cada persona tiene su propia foto). Se
     * corrige a Persona (1) -- (0..1) Imagen: cada persona tiene, como
     * maximo, una imagen propia. Ver el analisis de diseño presentado antes
     * de este codigo para el detalle completo de la correccion.
     *
     * @NotAudited: no se audita el contenido binario de la imagen (ver
     * Imagen.java): duplicaria el BLOB completo en persona_AUD /imagen_AUD
     * en cada cambio, sin aportar valor historico.
     */
    @NotAudited
    @OneToOne(fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "imagen_id")
    protected Imagen imagen;

    /**
     * Relacion "integrante de grupo familiar" (ver GrupoFamiliar.java para
     * la relacion inversa "familiares: List<Persona>" del diagrama
     * original). Nullable: una Persona puede no pertenecer todavia a ningun
     * grupo familiar.
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_familiar_id")
    protected GrupoFamiliar grupoFamiliar;
}
