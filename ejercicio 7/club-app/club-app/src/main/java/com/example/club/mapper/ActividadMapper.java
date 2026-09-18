package com.example.club.mapper;

import com.example.club.dto.ActividadDTO;
import com.example.club.entity.Actividad;
import org.springframework.stereotype.Component;

@Component
public class ActividadMapper {

    public ActividadDTO toDTO(Actividad actividad, long inscriptosActivos) {
        if (actividad == null) {
            return null;
        }
        return ActividadDTO.builder()
                .id(actividad.getId())
                .nombre(actividad.getNombre())
                .horario(actividad.getHorario())
                .cupos(actividad.getCupos())
                .inscriptosActivos(inscriptosActivos)
                .eliminado(actividad.isEliminado())
                .build();
    }

    public Actividad toEntity(ActividadDTO dto) {
        return Actividad.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .horario(dto.getHorario())
                .cupos(dto.getCupos())
                .eliminado(dto.isEliminado())
                .build();
    }
}
