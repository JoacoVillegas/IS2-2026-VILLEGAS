package com.example.colegio.mapper;

import com.example.colegio.dto.NotaDTO;
import com.example.colegio.entity.Nota;
import org.springframework.stereotype.Component;

@Component
public class NotaMapper {

    public NotaDTO toDTO(Nota nota) {
        if (nota == null) {
            return null;
        }
        return NotaDTO.builder()
                .id(nota.getIdNota())
                .fecha(nota.getFecha())
                .valor(nota.getValor())
                .alumnoId(nota.getAlumno().getId())
                .materiaId(nota.getMateria().getIdMateria())
                .alumnoNombreCompleto(nota.getAlumno().getNombre() + " " + nota.getAlumno().getApellido())
                .materiaNombre(nota.getMateria().getNombre())
                .build();
    }
}
