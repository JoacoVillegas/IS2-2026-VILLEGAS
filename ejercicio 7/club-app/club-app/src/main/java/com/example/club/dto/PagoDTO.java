package com.example.club.dto;

import com.example.club.entity.MedioPago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO {

    private Long id;

    @NotNull(message = "Debe seleccionar una familia")
    private Long grupoFamiliarId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El periodo es obligatorio")
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "El periodo debe tener el formato AAAA-MM, ej: 2026-09")
    private String periodo;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser mayor a cero")
    private BigDecimal importe;

    @NotNull(message = "Debe seleccionar un medio de pago")
    private MedioPago medioPago;

    private String comprobante;

    private String estado;

    /** Solo lectura, para listados. */
    private String familiaDescripcion;
}
