package com.example.colegio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO exclusivo del formulario "Cambiar contraseña" (nunca se persiste tal cual). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambioPasswordDTO {

    @NotBlank(message = "Debe ingresar su contraseña actual")
    private String passwordActual;

    @NotBlank(message = "Debe ingresar la nueva contraseña")
    @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
    private String passwordNueva;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String passwordConfirmacion;
}
