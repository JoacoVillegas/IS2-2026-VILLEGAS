package com.example.colegio.mapper;

import com.example.colegio.dto.AlumnoDTO;
import com.example.colegio.entity.Alumno;
import com.example.colegio.entity.Aula;
import org.springframework.stereotype.Component;

@Component
public class AlumnoMapper {

    public AlumnoDTO toDTO(Alumno alumno) {
        if (alumno == null) {
            return null;
        }
        Aula aula = alumno.getAula();
        String aulaDescripcion = (aula != null)
                ? aula.getGrado().getNivel() + " \"" + aula.getDivision() + "\""
                : null;
        return AlumnoDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .fechaNacimiento(alumno.getFechaNacimiento())
                .aulaId(aula != null ? aula.getIdAula() : null)
                .aulaDescripcion(aulaDescripcion)
                .eliminado(alumno.isEliminado())
                .build();
    }

    /** No asigna el Aula: AlumnoServiceImpl la resuelve via AulaService (regla Service->Service). */
    public void actualizarEntidadDesdeDTO(Alumno alumno, AlumnoDTO dto) {
        alumno.setNombre(dto.getNombre());
        alumno.setApellido(dto.getApellido());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
    }
}
