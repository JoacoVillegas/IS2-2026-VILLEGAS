package com.example.colegio.service.impl;

import com.example.colegio.dto.CursoDTO;
import com.example.colegio.entity.Aula;
import com.example.colegio.entity.Grado;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.mapper.CursoMapper;
import com.example.colegio.repository.AlumnoRepository;
import com.example.colegio.repository.AulaRepository;
import com.example.colegio.service.CursoService;
import com.example.colegio.service.GradoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa el ABM de "Curso" (entidad Aula, ver Aula.java / CursoDTO.java).
 *
 * NOTA DE ARQUITECTURA: para resolver el Grado al que pertenece un curso, NO
 * se inyecta GradoRepository aqui. Se inyecta GradoService y se usa su
 * metodo interno obtenerEntidadPorId(...), respetando estrictamente la regla
 * "un Service no accede al Repository de otra entidad" (CursoService ->
 * GradoService -> GradoRepository, nunca CursoService -> GradoRepository).
 *
 * Tambien se inyecta AlumnoRepository SOLO para el conteo de alumnos por
 * aula (countByAula_IdAulaAndEliminadoFalse), que es una consulta de solo
 * lectura auxiliar para mostrar "cantidad de alumnos" en el listado de
 * cursos. Se documenta como una excepcion consciente y acotada a una unica
 * consulta de conteo (no se manipulan Alumnos desde aqui); en un proyecto de
 * mayor escala esto se resolveria exponiendo
 * AlumnoService.contarPorAula(aulaId) en lugar de tocar el repository.
 */
@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private final AulaRepository aulaRepository;
    private final AlumnoRepository alumnoRepository;
    private final GradoService gradoService;
    private final CursoMapper cursoMapper;

    public CursoServiceImpl(AulaRepository aulaRepository,
                             AlumnoRepository alumnoRepository,
                             GradoService gradoService,
                             CursoMapper cursoMapper) {
        this.aulaRepository = aulaRepository;
        this.alumnoRepository = alumnoRepository;
        this.gradoService = gradoService;
        this.cursoMapper = cursoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> listarActivos() {
        return aulaRepository.findByEliminadoFalse().stream()
                .map(aula -> cursoMapper.toDTO(aula, contarAlumnos(aula.getIdAula())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDTO buscarPorId(Long id) {
        Aula aula = obtenerActivaPorId(id);
        return cursoMapper.toDTO(aula, contarAlumnos(id));
    }

    @Override
    public CursoDTO registrar(CursoDTO dto) {
        Grado grado = gradoService.obtenerEntidadPorId(dto.getGradoId());
        Aula aula = Aula.builder()
                .division(dto.getDivision())
                .grado(grado)
                .eliminado(false)
                .build();
        Aula guardada = aulaRepository.save(aula);
        return cursoMapper.toDTO(guardada, 0);
    }

    @Override
    public CursoDTO actualizar(Long id, CursoDTO dto) {
        Aula aula = obtenerActivaPorId(id);
        cursoMapper.actualizarEntidadDesdeDTO(aula, dto);
        if (!aula.getGrado().getIdGrado().equals(dto.getGradoId())) {
            aula.setGrado(gradoService.obtenerEntidadPorId(dto.getGradoId()));
        }
        return cursoMapper.toDTO(aulaRepository.save(aula), contarAlumnos(id));
    }

    @Override
    public void eliminar(Long id) {
        Aula aula = obtenerActivaPorId(id);
        aula.setEliminado(true); // baja logica
        aulaRepository.save(aula);
    }

    @Override
    @Transactional(readOnly = true)
    public Aula obtenerEntidadPorId(Long id) {
        return obtenerActivaPorId(id);
    }

    private Aula obtenerActivaPorId(Long id) {
        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado, id: " + id));
        if (aula.isEliminado()) {
            throw new ResourceNotFoundException("El curso con id " + id + " fue dado de baja");
        }
        return aula;
    }

    private int contarAlumnos(Long aulaId) {
        return (int) alumnoRepository.countByAula_IdAulaAndEliminadoFalse(aulaId);
    }
}
