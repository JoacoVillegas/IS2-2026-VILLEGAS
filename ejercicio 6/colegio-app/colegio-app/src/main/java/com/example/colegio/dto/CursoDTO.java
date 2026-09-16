package com.example.colegio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * "CursoDTO" transporta los datos de la entidad Aula (ver Aula.java para la
 * justificacion de por que "Curso" se implementa sobre la clase "Aula" del
 * diagrama original). Se llama "CursoDTO" -y no "AulaDTO"- deliberadamente,
 * porque es el nombre con el que el usuario final ve este concepto en las
 * vistas y en las URLs (/admin/cursos).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {

    private Long id;

    @NotBlank(message = "La division es obligatoria (ej: 'A')")
    private String division;

    @NotNull(message = "Debe seleccionar un grado")
    private Long gradoId;

    /** Solo lectura, para listados: "gradoNivel" + "division" -> "3er grado A". */
    private String gradoNivel;

    private int cantidadAlumnos;

    private boolean eliminado;
}
