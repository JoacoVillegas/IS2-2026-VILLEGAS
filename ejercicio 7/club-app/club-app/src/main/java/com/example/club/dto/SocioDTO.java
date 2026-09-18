package com.example.club.dto;

import com.example.club.entity.EstadoSocio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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
public class SocioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de alta es obligatoria")
    private LocalDate fechaAlta;

    @NotNull(message = "El estado es obligatorio")
    private EstadoSocio estado;

    /** Id del GrupoFamiliar del cual este socio es titular (solo lectura, se crea automaticamente al registrar). */
    private Long grupoFamiliarId;

    private boolean tieneImagen;

    private boolean eliminado;
}
