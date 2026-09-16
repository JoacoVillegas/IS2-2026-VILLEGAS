package com.example.colegio.dto;

import com.example.colegio.entity.Sexo;
import jakarta.validation.constraints.Email;
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
 * QUE ES UN DTO (Data Transfer Object) Y POR QUE SE USA:
 * Un DTO es un objeto "plano" cuyo unico proposito es transportar datos entre
 * capas (Controller <-> Service <-> Vista), sin exponer la estructura interna
 * de persistencia (la entidad JPA). Se usa aqui, en lugar de pasar
 * directamente la entidad Profesor, por tres razones concretas:
 *   1. Seguridad: la entidad Profesor contiene un Usuario con passwordHash;
 *      un DTO permite elegir exactamente que campos viajan a la vista
 *      (nunca el hash de la contraseña).
 *   2. Desacople: si mañana cambia el modelo de persistencia (por ejemplo,
 *      se separa "especialidad" en otra tabla), las vistas Thymeleaf y los
 *      Controllers no se ven afectados mientras el DTO mantenga su forma.
 *   3. Validacion de formularios: las anotaciones de Jakarta Validation
 *      (@NotBlank, @Email, etc.) se ubican en el DTO porque representan
 *      reglas de "entrada de datos desde la vista", no reglas del modelo de
 *      persistencia en si.
 *
 * DIFERENCIA ENTITY vs DTO: la Entity (Profesor.java) esta anotada con JPA
 * (@Entity, @Table, @ManyToOne, etc.) y representa una fila de la tabla
 * "profesor"; el DTO no tiene ninguna anotacion de persistencia y representa
 * los datos tal como los necesita el formulario HTML / la vista.
 *
 * CONVERSION Entity <-> DTO: se realiza en la capa "mapper"
 * (ver ProfesorMapper.java), invocada desde ProfesorServiceImpl. El
 * Controller nunca convierte manualmente Entity<->DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotNull(message = "El sexo es obligatorio")
    private Sexo sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @Size(max = 100)
    private String especialidad;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    private String correo;

    /**
     * Solo se usa al REGISTRAR un profesor nuevo (contraseña inicial). Al
     * editar un profesor existente este campo llega null/vacio y
     * ProfesorServiceImpl simplemente no toca la contraseña ya guardada.
     */
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String passwordInicial;

    private boolean eliminado;
}
