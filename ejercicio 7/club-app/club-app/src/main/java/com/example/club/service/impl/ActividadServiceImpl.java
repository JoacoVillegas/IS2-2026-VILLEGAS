package com.example.club.service.impl;

import com.example.club.entity.Actividad;
import com.example.club.entity.EstadoInscripcion;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.mapper.ActividadMapper;
import com.example.club.repository.ActividadRepository;
import com.example.club.repository.InscripcionRepository;
import com.example.club.service.ActividadService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * NOTA DE ARQUITECTURA: ActividadServiceImpl usa InscripcionRepository
 * ademas de ActividadRepository, unicamente para el CONTEO de inscriptos
 * activos (metodo contarInscriptosActivos), expuesto como metodo publico
 * de este mismo Service para que InscripcionServiceImpl lo consulte sin
 * acceder el mismo a InscripcionRepository desde otro dominio. Se trata como
 * responsabilidad combinada "Actividad (y su ocupacion)", igual criterio
 * documentado en CursoServiceImpl del proyecto de colegio.
 */
@Service
@Transactional
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ActividadMapper actividadMapper;

    public ActividadServiceImpl(ActividadRepository actividadRepository,
                                 InscripcionRepository inscripcionRepository,
                                 ActividadMapper actividadMapper) {
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.actividadMapper = actividadMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.example.club.dto.ActividadDTO> listarActivas() {
        return actividadRepository.findByEliminadoFalse().stream()
                .map(a -> actividadMapper.toDTO(a, contarInscriptosActivos(a.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public com.example.club.dto.ActividadDTO buscarPorId(Long id) {
        Actividad actividad = obtenerActivaPorId(id);
        return actividadMapper.toDTO(actividad, contarInscriptosActivos(id));
    }

    @Override
    public com.example.club.dto.ActividadDTO registrar(com.example.club.dto.ActividadDTO dto) {
        Actividad guardada = actividadRepository.save(actividadMapper.toEntity(dto));
        return actividadMapper.toDTO(guardada, 0);
    }

    @Override
    public com.example.club.dto.ActividadDTO actualizar(Long id, com.example.club.dto.ActividadDTO dto) {
        Actividad actividad = obtenerActivaPorId(id);
        actividad.setNombre(dto.getNombre());
        actividad.setHorario(dto.getHorario());
        actividad.setCupos(dto.getCupos());
        Actividad guardada = actividadRepository.save(actividad);
        return actividadMapper.toDTO(guardada, contarInscriptosActivos(id));
    }

    @Override
    public void eliminar(Long id) {
        Actividad actividad = obtenerActivaPorId(id);
        actividad.setEliminado(true);
        actividadRepository.save(actividad);
    }

    @Override
    @Transactional(readOnly = true)
    public Actividad obtenerEntidadPorId(Long id) {
        return obtenerActivaPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarInscriptosActivos(Long actividadId) {
        return inscripcionRepository.countByActividad_IdAndEstado(actividadId, EstadoInscripcion.ACTIVA);
    }

    private Actividad obtenerActivaPorId(Long id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada, id: " + id));
        if (actividad.isEliminado()) {
            throw new ResourceNotFoundException("La actividad con id " + id + " fue dada de baja");
        }
        return actividad;
    }
}
