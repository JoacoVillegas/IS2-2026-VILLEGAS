package com.example.club.mapper;

import com.example.club.dto.PersonaDTO;
import com.example.club.entity.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaDTO toDTO(Persona persona) {
        if (persona == null) {
            return null;
        }
        return PersonaDTO.builder()
                .id(persona.getId())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .fechaNacimiento(persona.getFechaNacimiento())
                .grupoFamiliarId(persona.getGrupoFamiliar() != null ? persona.getGrupoFamiliar().getId() : null)
                .tieneImagen(persona.getImagen() != null)
                .eliminado(persona.isEliminado())
                .build();
    }

    public void actualizarEntidadDesdeDTO(Persona persona, PersonaDTO dto) {
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setFechaNacimiento(dto.getFechaNacimiento());
    }
}
