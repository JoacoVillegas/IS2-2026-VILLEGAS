package com.example.club.dto;

import jakarta.validation.constraints.NotNull;
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
public class InscripcionDTO {

    private Long id;

    @NotNull(message = "Debe seleccionar un socio")
    private Long socioId;

    @NotNull(message = "Debe seleccionar una actividad")
    private Long actividadId;

    private LocalDate fechaInscripcion;
    private String estado;

    /** Solo lectura, para listados. */
    private String socioNombreCompleto;
    private String actividadNombre;
}
