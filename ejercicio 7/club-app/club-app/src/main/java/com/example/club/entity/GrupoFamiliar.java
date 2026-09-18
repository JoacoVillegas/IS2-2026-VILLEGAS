package com.example.club.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * GrupoFamiliar (ver analisis de diseño: se distinguen dos relaciones que el
 * diagrama superponia en una unica flecha):
 *
 *  1) TITULARIDAD: un Socio es el titular de exactamente un GrupoFamiliar
 *     (composicion del diagrama original). Se modela como @OneToOne desde
 *     GrupoFamiliar hacia Socio, con FK UNICA "socio_titular_id".
 *  2) INTEGRANTES: la lista "familiares: List<Persona>" del diagrama se
 *     modela como @OneToMany (lado inverso de Persona.grupoFamiliar),
 *     representando a todas las Personas del grupo (incluyendo,
 *     opcionalmente, al propio socio titular si tambien se lo carga como
 *     integrante).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grupo_familiar")
@Audited
public class GrupoFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @NotAudited: se decide no auditar el cambio de titular (evento poco
     * frecuente y de bajo valor historico para este ejercicio), igual
     * criterio que la relacion Alumno-Aula en el proyecto de colegio.
     */
    @NotAudited
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "socio_titular_id", unique = true, nullable = false)
    private Socio socioTitular;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "grupoFamiliar", fetch = FetchType.LAZY)
    private List<Persona> familiares = new ArrayList<>();

    /**
     * NOTA: el campo "eliminado" no formaba parte del diagrama original
     * para esta clase; se agrega por consistencia con el resto del dominio
     * (mismo criterio de baja logica uniforme) -- DECISION DE DISENO.
     */
    @Column(nullable = false)
    private boolean eliminado = false;
}
