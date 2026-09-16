package com.example.colegio.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
public class NotaDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El valor de la nota es obligatorio")
    @DecimalMin(value = "0.0", message = "La nota minima es 0")
    @DecimalMax(value = "10.0", message = "La nota maxima es 10")
    private Float valor;

    @NotNull(message = "Debe seleccionar un alumno")
    private Long alumnoId;

    @NotNull(message = "Debe seleccionar una materia")
    private Long materiaId;

    /** Solo lectura para listados. */
    private String alumnoNombreCompleto;
    private String materiaNombre;
}
