package com.example.club.dto;

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

/**
 * QUE ES UN DTO Y POR QUE SE USA (ver tambien ProfesorDTO.java del proyecto
 * de colegio, usado como referencia): un DTO transporta datos entre capas
 * sin exponer la estructura de persistencia. Se usa aqui, en lugar de la
 * entidad Persona, porque:
 *  1. La entidad tiene relaciones JPA (imagen, grupoFamiliar) que no deben
 *     viajar tal cual hacia un formulario Thymeleaf.
 *  2. Las anotaciones de Jakarta Validation (@NotBlank, @Past, etc.)
 *     pertenecen a "reglas de entrada de datos desde la vista", no al
 *     modelo de persistencia.
 * DIFERENCIA Entity/DTO: Persona.java (entidad) tiene @Entity/@Inheritance/
 * relaciones JPA; este DTO es un objeto plano sin ninguna anotacion de
 * persistencia.
 * CONVERSION Entity<->DTO: se realiza en PersonaMapper.java, invocado desde
 * los *ServiceImpl. Los Controllers nunca convierten manualmente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaDTO {

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

    private Long grupoFamiliarId;

    /** true si esta Persona ya tiene una Imagen cargada (para mostrar/ocultar el link "ver foto"). */
    private boolean tieneImagen;

    private boolean eliminado;
}
