package com.example.club.service;

import com.example.club.dto.PersonaDTO;
import com.example.club.entity.Persona;
import java.util.List;

/** Gestiona Personas "puras" (integrantes de un grupo familiar que no son Socio). */
public interface PersonaService {

    List<PersonaDTO> listarActivas();

    PersonaDTO buscarPorId(Long id);

    PersonaDTO registrar(PersonaDTO dto);

    PersonaDTO actualizar(Long id, PersonaDTO dto);

    void eliminar(Long id);

    /** Uso interno de otros Services (RegistroAccesoServiceImpl, ImagenServiceImpl). */
    Persona obtenerEntidadPorId(Long id);
}
