package com.example.club.service;

import com.example.club.dto.SocioDTO;
import com.example.club.entity.Socio;
import java.util.List;

/**
 * Registrar un Socio implica, ademas, crear automaticamente su
 * GrupoFamiliar (composicion, ver analisis de diseño): por eso
 * SocioServiceImpl colabora con GrupoFamiliarService (Service->Service,
 * nunca Service->Repository de otra entidad).
 */
public interface SocioService {

    List<SocioDTO> listarActivos();

    SocioDTO buscarPorId(Long id);

    SocioDTO registrar(SocioDTO dto);

    SocioDTO actualizar(Long id, SocioDTO dto);

    void eliminar(Long id);

    /** Uso interno de otros Services (GrupoFamiliarServiceImpl, InscripcionServiceImpl, PagoServiceImpl). */
    Socio obtenerEntidadPorId(Long id);
}
