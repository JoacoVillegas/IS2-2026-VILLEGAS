package com.example.club.mapper;

import com.example.club.dto.GrupoFamiliarDTO;
import com.example.club.entity.GrupoFamiliar;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GrupoFamiliarMapper {

    private final PersonaMapper personaMapper;

    public GrupoFamiliarMapper(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    public GrupoFamiliarDTO toDTO(GrupoFamiliar grupo) {
        if (grupo == null) {
            return null;
        }
        List<com.example.club.dto.PersonaDTO> familiares = grupo.getFamiliares().stream()
                .map(personaMapper::toDTO)
                .toList();
        return GrupoFamiliarDTO.builder()
                .id(grupo.getId())
                .socioTitularId(grupo.getSocioTitular().getId())
                .socioTitularNombreCompleto(grupo.getSocioTitular().getNombre() + " " + grupo.getSocioTitular().getApellido())
                .familiares(familiares)
                .eliminado(grupo.isEliminado())
                .build();
    }
}
