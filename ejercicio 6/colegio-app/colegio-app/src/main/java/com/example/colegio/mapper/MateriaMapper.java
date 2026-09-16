package com.example.colegio.mapper;

import com.example.colegio.dto.MateriaDTO;
import com.example.colegio.entity.Materia;
import org.springframework.stereotype.Component;

@Component
public class MateriaMapper {

    public MateriaDTO toDTO(Materia materia) {
        if (materia == null) {
            return null;
        }
        return MateriaDTO.builder()
                .id(materia.getIdMateria())
                .nombre(materia.getNombre())
                .eliminado(materia.isEliminado())
                .build();
    }

    public Materia toEntity(MateriaDTO dto) {
        return Materia.builder()
                .idMateria(dto.getId())
                .nombre(dto.getNombre())
                .eliminado(dto.isEliminado())
                .build();
    }
}
