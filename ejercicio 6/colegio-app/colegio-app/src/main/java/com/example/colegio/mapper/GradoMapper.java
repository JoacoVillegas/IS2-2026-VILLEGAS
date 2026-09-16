package com.example.colegio.mapper;

import com.example.colegio.dto.GradoDTO;
import com.example.colegio.entity.Grado;
import org.springframework.stereotype.Component;

@Component
public class GradoMapper {

    public GradoDTO toDTO(Grado grado) {
        if (grado == null) {
            return null;
        }
        return GradoDTO.builder()
                .id(grado.getIdGrado())
                .nivel(grado.getNivel())
                .eliminado(grado.isEliminado())
                .build();
    }
}
