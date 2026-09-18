package com.example.club.mapper;

import com.example.club.dto.RegistroAccesoDTO;
import com.example.club.entity.RegistroAcceso;
import org.springframework.stereotype.Component;

@Component
public class RegistroAccesoMapper {

    public RegistroAccesoDTO toDTO(RegistroAcceso registro) {
        if (registro == null) {
            return null;
        }
        var duracion = registro.obtenerDuracion();
        return RegistroAccesoDTO.builder()
                .id(registro.getId())
                .personaId(registro.getPersona().getId())
                .fecha(registro.getFecha())
                .horaEntrada(registro.getHoraEntrada())
                .horaSalida(registro.getHoraSalida())
                .personaNombreCompleto(registro.getPersona().getNombre() + " " + registro.getPersona().getApellido())
                .duracion(duracion != null ? duracion.toMinutes() + " min" : "En curso")
                .build();
    }
}
