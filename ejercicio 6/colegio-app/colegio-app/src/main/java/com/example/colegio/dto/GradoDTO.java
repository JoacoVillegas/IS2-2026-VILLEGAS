package com.example.colegio.dto;

import jakarta.validation.constraints.NotBlank;
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
public class GradoDTO {
    private Long id;

    @NotBlank(message = "El nivel es obligatorio (ej: '1er grado')")
    private String nivel;

    private boolean eliminado;
}
