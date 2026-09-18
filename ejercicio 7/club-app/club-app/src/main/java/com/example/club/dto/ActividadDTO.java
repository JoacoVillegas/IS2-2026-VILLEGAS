package com.example.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;
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
public class ActividadDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El horario es obligatorio")
    private LocalTime horario;

    @Positive(message = "Los cupos deben ser un numero positivo")
    private long cupos;

    /** Solo lectura: cuantas inscripciones ACTIVAS tiene hoy (cupos.disponibles = cupos - inscriptos). */
    private long inscriptosActivos;

    private boolean eliminado;
}
