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
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * NUEVA ENTIDAD (no estaba en el diagrama original) para el requisito de
 * pago de cuotas. Ver el analisis de diseño presentado antes del codigo,
 * seccion 4 y 13, para la justificacion completa de por que se modela
 * "Familia -> Pago" (sin una entidad "Cuota" intermedia): se prioriza
 * evitar complejidad innecesaria, ya que el enunciado pide registrar el
 * pago, no administrar planes de deuda o pagos parciales.
 *
 * @Audited: es CRITICO auditar esta entidad -- cualquier alta o anulacion
 * de un pago debe quedar trazada (quien la hizo y cuando), por tratarse de
 * informacion con impacto contable/administrativo real.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pago")
@Audited
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * REQUISITO DEL ENUNCIADO (punto 12): el pago pertenece a la FAMILIA,
     * nunca a un integrante en particular. Por eso NO existe ninguna
     * relacion Pago -> Persona/Socio.
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_familiar_id", nullable = false)
    private GrupoFamiliar grupoFamiliar;

    @Column(nullable = false)
    private LocalDate fecha;

    /**
     * Periodo que este pago cubre, en formato "yyyy-MM" (ej: "2026-09").
     * Se guarda como String (en vez de agregar una entidad Cuota) por la
     * decision de diseño explicada arriba: evita el modelo mas complejo
     * Familia->Cuota->Pago cuando el enunciado no exige administrar deuda.
     */
    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    /** REQUISITO EXPLICITO del enunciado: EFECTIVO, TRANSFERENCIA o MERCADO_PAGO. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MedioPago medioPago;

    /** Permite anular (baja logica) un pago cargado por error, sin borrarlo. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado;

    /** Numero de comprobante/referencia, opcional (ej. numero de transferencia). */
    @Column(length = 100)
    private String comprobante;
}
