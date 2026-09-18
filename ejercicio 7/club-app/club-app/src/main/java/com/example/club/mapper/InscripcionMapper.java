package com.example.club.mapper;

import com.example.club.dto.InscripcionDTO;
import com.example.club.entity.Inscripcion;
import org.springframework.stereotype.Component;

@Component
public class InscripcionMapper {

    public InscripcionDTO toDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .socioId(inscripcion.getSocio().getId())
                .actividadId(inscripcion.getActividad().getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .estado(inscripcion.getEstado().name())
                .socioNombreCompleto(inscripcion.getSocio().getNombre() + " " + inscripcion.getSocio().getApellido())
                .actividadNombre(inscripcion.getActividad().getNombre())
                .build();
    }
}
