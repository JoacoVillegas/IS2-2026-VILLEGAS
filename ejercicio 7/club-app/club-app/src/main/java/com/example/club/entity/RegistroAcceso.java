package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RegistroAcceso: se respeta el diseño del diagrama original, que ya
 * unifica entrada y salida en una unica entidad (en lugar de dos entidades
 * separadas "RegistroIngreso"/"RegistroSalida"). Esto es mas simple: cada
 * fila representa "una visita al club" con su hora de entrada y,
 * eventualmente, su hora de salida (null hasta que la persona se retira).
 *
 * DECISION DE DISENO: esta entidad NO se audita con Envers (@Audited
 * ausente a proposito). Se trata de un registro de alto volumen (se crea en
 * cada entrada/salida de cada persona, todos los dias); auditar cada
 * modificacion duplicaria practicamente todo el volumen de datos sin
 * aportar valor historico adicional al que la propia fila ya conserva.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "registro_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaEntrada;

    /** Null mientras la persona sigue dentro del club. */
    private LocalTime horaSalida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    /**
     * Metodo de dominio presente en el diagrama original
     * ("+obtenerDuracion()"). Se mantiene aqui porque es un calculo
     * derivado exclusivamente de los propios datos de la entidad.
     */
    public Duration obtenerDuracion() {
        if (horaSalida == null) {
            return null; // la persona todavia no registro su salida
        }
        return Duration.between(horaEntrada, horaSalida);
    }
}
