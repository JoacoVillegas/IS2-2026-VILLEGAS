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
import org.hibernate.envers.NotAudited;

/**
 * "Grado" = nivel/año escolar (ej: "1er grado", "2do grado"). Segun el
 * diagrama: Colegio (1) -- (*) Grado, y Grado (1) -- (1..*) Aula.
 *
 * No se pidio ABM explicito de "Grado" en el enunciado (solo se pide ABM de
 * "Cursos"), pero Grado es indispensable como agrupador de Aulas, asi que se
 * ofrece una gestion minima (alta/listado) desde la pantalla de Cursos, sin
 * una pantalla propia dedicada: ver AulaController / vista cursos, donde el
 * formulario de "Curso" permite elegir o crear el Grado al que pertenece.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grado")
@Audited
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrado;

    @Column(nullable = false, length = 50)
    private String nivel;

    @Column(nullable = false)
    private boolean eliminado = false;

    /**
     * @ManyToOne: muchos Grados pertenecen a un unico Colegio.
     * FetchType.LAZY: Hibernate NO trae el Colegio de la base de datos hasta
     * que se acceda explicitamente a getColegio() (evita cargar datos que no
     * siempre se necesitan y mejora el rendimiento de las consultas de
     * listado).
     * @JoinColumn: nombre de la columna FK (Foreign Key) en la tabla "grado".
     *
     * @NotAudited: se excluye la referencia al Colegio de la tabla grado_AUD
     * porque, al haber un unico colegio en el sistema (ver Colegio.java), esa
     * relacion nunca cambia y auditarla no aporta informacion util.
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id", nullable = false)
    private Colegio colegio;
}
