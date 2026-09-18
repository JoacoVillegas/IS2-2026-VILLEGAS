package com.example.club.dto;

import java.util.List;
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
public class GrupoFamiliarDTO {

    private Long id;
    private Long socioTitularId;
    private String socioTitularNombreCompleto;

    /** Solo lectura: integrantes actuales (para la vista de detalle de familia). */
    private List<PersonaDTO> familiares;

    private boolean eliminado;
}
