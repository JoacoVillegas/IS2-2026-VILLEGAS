package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * Socio extiende Persona mediante herencia JPA JOINED (ver Persona.java).
 * Hibernate crea una tabla "socio" con SOLO las columnas propias
 * (fechaAlta, estado) mas una columna "id" que es a la vez PK de "socio" y
 * FK hacia "persona" (comparten el mismo identificador).
 *
 * @Audited: se audita igual que Persona (de la que hereda), ver README para
 * el detalle de que entidades se auditan.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "socio")
@Audited
public class Socio extends Persona {

    @Column(nullable = false)
    private LocalDate fechaAlta;

    /**
     * @Enumerated(EnumType.STRING): guarda el nombre del enum ("ACTIVO",
     * "INACTIVO") como texto en la columna, en lugar del indice numerico
     * (EnumType.ORDINAL). Se prefiere STRING porque es legible directamente
     * en la base de datos y no se rompe si en el futuro se reordenan los
     * valores del enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSocio estado;
}
