package com.example.club.mapper;

import com.example.club.dto.SocioDTO;
import com.example.club.entity.Socio;
import org.springframework.stereotype.Component;

@Component
public class SocioMapper {

    public SocioDTO toDTO(Socio socio, Long grupoFamiliarId) {
        if (socio == null) {
            return null;
        }
        return SocioDTO.builder()
                .id(socio.getId())
                .nombre(socio.getNombre())
                .apellido(socio.getApellido())
                .fechaNacimiento(socio.getFechaNacimiento())
                .fechaAlta(socio.getFechaAlta())
                .estado(socio.getEstado())
                .grupoFamiliarId(grupoFamiliarId)
                .tieneImagen(socio.getImagen() != null)
                .eliminado(socio.isEliminado())
                .build();
    }

    public void actualizarEntidadDesdeDTO(Socio socio, SocioDTO dto) {
        socio.setNombre(dto.getNombre());
        socio.setApellido(dto.getApellido());
        socio.setFechaNacimiento(dto.getFechaNacimiento());
        socio.setEstado(dto.getEstado());
    }
}
