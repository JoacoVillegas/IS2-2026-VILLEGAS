package com.example.club.service.impl;

import com.example.club.dto.PersonaDTO;
import com.example.club.entity.Persona;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.PersonaMapper;
import com.example.club.repository.PersonaRepository;
import com.example.club.service.PersonaService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO> listarActivas() {
        return personaRepository.findByEliminadoFalse().stream().map(personaMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaDTO buscarPorId(Long id) {
        return personaMapper.toDTO(obtenerActivaPorId(id));
    }

    @Override
    public PersonaDTO registrar(PersonaDTO dto) {
        Persona persona = Persona.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .fechaNacimiento(dto.getFechaNacimiento())
                .eliminado(false)
                .build();
        return personaMapper.toDTO(personaRepository.save(persona));
    }

    @Override
    public PersonaDTO actualizar(Long id, PersonaDTO dto) {
        Persona persona = obtenerActivaPorId(id);
        personaMapper.actualizarEntidadDesdeDTO(persona, dto);
        return personaMapper.toDTO(personaRepository.save(persona));
    }

    @Override
    public void eliminar(Long id) {
        Persona persona = obtenerActivaPorId(id);
        persona.setEliminado(true);
        personaRepository.save(persona);
    }

    @Override
    @Transactional(readOnly = true)
    public Persona obtenerEntidadPorId(Long id) {
        return obtenerActivaPorId(id);
    }

    private Persona obtenerActivaPorId(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada, id: " + id));
        if (persona.isEliminado()) {
            throw new ResourceNotFoundException("La persona con id " + id + " fue dada de baja");
        }
        return persona;
    }
}
