package com.example.colegio.mapper;

import com.example.colegio.dto.CursoDTO;
import com.example.colegio.entity.Aula;
import org.springframework.stereotype.Component;

/** Mapea Aula <-> CursoDTO (ver Aula.java para la justificacion del nombre "Curso"). */
@Component
public class CursoMapper {

    public CursoDTO toDTO(Aula aula, int cantidadAlumnos) {
        if (aula == null) {
            return null;
        }
        return CursoDTO.builder()
                .id(aula.getIdAula())
                .division(aula.getDivision())
                .gradoId(aula.getGrado().getIdGrado())
                .gradoNivel(aula.getGrado().getNivel())
                .cantidadAlumnos(aula.obtenerCantidadAlumnos(cantidadAlumnos))
                .eliminado(aula.isEliminado())
                .build();
    }

    public void actualizarEntidadDesdeDTO(Aula aula, CursoDTO dto) {
        aula.setDivision(dto.getDivision());
    }
}
