package com.example.club.service.impl;

import com.example.club.dto.GrupoFamiliarDTO;
import com.example.club.entity.GrupoFamiliar;
import com.example.club.entity.Persona;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.GrupoFamiliarMapper;
import com.example.club.repository.GrupoFamiliarRepository;
import com.example.club.service.GrupoFamiliarService;
import com.example.club.service.PersonaService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrupoFamiliarServiceImpl implements GrupoFamiliarService {

    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final GrupoFamiliarMapper grupoFamiliarMapper;
    // Service->Service (no PersonaRepository directo), respetando la regla de arquitectura.
    private final PersonaService personaService;

    public GrupoFamiliarServiceImpl(GrupoFamiliarRepository grupoFamiliarRepository,
                                     GrupoFamiliarMapper grupoFamiliarMapper,
                                     PersonaService personaService) {
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.grupoFamiliarMapper = grupoFamiliarMapper;
        this.personaService = personaService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoFamiliarDTO> listarActivos() {
        return grupoFamiliarRepository.findByEliminadoFalse().stream()
                .map(grupoFamiliarMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoFamiliarDTO buscarPorId(Long id) {
        return grupoFamiliarMapper.toDTO(obtenerActivoPorId(id));
    }

    @Override
    public void agregarFamiliar(Long grupoFamiliarId, Long personaId) {
        GrupoFamiliar grupo = obtenerActivoPorId(grupoFamiliarId);
        Persona persona = personaService.obtenerEntidadPorId(personaId);
        // Al estar dentro de una transaccion (@Transactional de esta clase;
        // PersonaService.obtenerEntidadPorId se ejecuta en la MISMA
        // transaccion por propagacion REQUIRED por defecto), "persona" sigue
        // siendo una entidad "managed" por Hibernate: no hace falta llamar a
        // ningun save() explicito. Hibernate detecta el cambio de estado
        // (dirty checking) y genera el UPDATE correspondiente al finalizar
        // la transaccion.
        persona.setGrupoFamiliar(grupo);
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoFamiliar obtenerEntidadPorId(Long id) {
        return obtenerActivoPorId(id);
    }

    private GrupoFamiliar obtenerActivoPorId(Long id) {
        GrupoFamiliar grupo = grupoFamiliarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo familiar no encontrado, id: " + id));
        if (grupo.isEliminado()) {
            throw new ResourceNotFoundException("El grupo familiar con id " + id + " fue dado de baja");
        }
        return grupo;
    }
}
