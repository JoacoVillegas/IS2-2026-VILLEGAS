package com.example.club.service;

import com.example.club.dto.GrupoFamiliarDTO;
import com.example.club.entity.GrupoFamiliar;
import java.util.List;

public interface GrupoFamiliarService {

    List<GrupoFamiliarDTO> listarActivos();

    GrupoFamiliarDTO buscarPorId(Long id);

    /** Agrega una Persona existente como integrante del grupo familiar. */
    void agregarFamiliar(Long grupoFamiliarId, Long personaId);

    /** Uso interno de otros Services (PagoServiceImpl, PersonaServiceImpl). */
    GrupoFamiliar obtenerEntidadPorId(Long id);
}
