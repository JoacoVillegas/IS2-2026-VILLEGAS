package com.example.colegio.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO de solo lectura para la pantalla "Mi perfil" del profesor logueado. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilProfesorDTO {
    private String nombre;
    private String apellido;
    private String especialidad;
    private String correo;
    private LocalDate fechaNacimiento;
}
