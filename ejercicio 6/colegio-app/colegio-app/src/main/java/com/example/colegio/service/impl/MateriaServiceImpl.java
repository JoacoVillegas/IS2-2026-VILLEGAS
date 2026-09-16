package com.example.colegio.service.impl;

import com.example.colegio.dto.MateriaDTO;
import com.example.colegio.entity.Materia;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.mapper.MateriaMapper;
import com.example.colegio.repository.MateriaRepository;
import com.example.colegio.service.MateriaService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final MateriaMapper materiaMapper;

    public MateriaServiceImpl(MateriaRepository materiaRepository, MateriaMapper materiaMapper) {
        this.materiaRepository = materiaRepository;
        this.materiaMapper = materiaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaDTO> listarActivas() {
        return materiaRepository.findByEliminadoFalse().stream().map(materiaMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MateriaDTO buscarPorId(Long id) {
        return materiaMapper.toDTO(obtenerActivaPorId(id));
    }

    @Override
    public MateriaDTO registrar(MateriaDTO dto) {
        materiaRepository.findByNombreIgnoreCase(dto.getNombre()).ifPresent(m -> {
            throw new IllegalArgumentException("Ya existe una materia llamada '" + dto.getNombre() + "'");
        });
        Materia guardada = materiaRepository.save(materiaMapper.toEntity(dto));
        return materiaMapper.toDTO(guardada);
    }

    @Override
    public MateriaDTO actualizar(Long id, MateriaDTO dto) {
        Materia materia = obtenerActivaPorId(id);
        materia.setNombre(dto.getNombre());
        return materiaMapper.toDTO(materiaRepository.save(materia));
    }

    @Override
    public void eliminar(Long id) {
        Materia materia = obtenerActivaPorId(id);
        materia.setEliminado(true); // baja logica
        materiaRepository.save(materia);
    }

    @Override
    @Transactional(readOnly = true)
    public Materia obtenerEntidadPorId(Long id) {
        return obtenerActivaPorId(id);
    }

    private Materia obtenerActivaPorId(Long id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada, id: " + id));
        if (materia.isEliminado()) {
            throw new ResourceNotFoundException("La materia con id " + id + " fue dada de baja");
        }
        return materia;
    }
}
