package com.example.club.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
public class RegistroAccesoDTO {

    private Long id;

    @NotNull(message = "Debe seleccionar una persona")
    private Long personaId;

    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    /** Solo lectura, para listados. */
    private String personaNombreCompleto;
    private String duracion;
}
