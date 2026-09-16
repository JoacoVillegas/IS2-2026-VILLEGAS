package com.example.colegio.service.impl;

import com.example.colegio.dto.AlumnoDTO;
import com.example.colegio.entity.Alumno;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.mapper.AlumnoMapper;
import com.example.colegio.repository.AlumnoRepository;
import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.CursoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * NOTA DE ARQUITECTURA: para resolver el Aula/Curso de un Alumno se inyecta
 * CursoService (no AulaRepository), respetando "Service A -> Service B ->
 * Repository B" (AlumnoService -> CursoService -> AulaRepository).
 */
@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final CursoService cursoService;
    private final AlumnoMapper alumnoMapper;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository, CursoService cursoService, AlumnoMapper alumnoMapper) {
        this.alumnoRepository = alumnoRepository;
        this.cursoService = cursoService;
        this.alumnoMapper = alumnoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> listarActivos() {
        return alumnoRepository.findByEliminadoFalse().stream().map(alumnoMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> listarPorAulas(List<Long> aulaIds) {
        return aulaIds.stream()
                .flatMap(aulaId -> alumnoRepository.findByAula_IdAulaAndEliminadoFalse(aulaId).stream())
                .map(alumnoMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoDTO buscarPorId(Long id) {
        return alumnoMapper.toDTO(obtenerActivoPorId(id));
    }

    @Override
    public AlumnoDTO registrar(AlumnoDTO dto) {
        Alumno alumno = Alumno.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .fechaNacimiento(dto.getFechaNacimiento())
                .aula(cursoService.obtenerEntidadPorId(dto.getAulaId()))
                .eliminado(false)
                .build();
        return alumnoMapper.toDTO(alumnoRepository.save(alumno));
    }

    @Override
    public AlumnoDTO actualizar(Long id, AlumnoDTO dto) {
        Alumno alumno = obtenerActivoPorId(id);
        alumnoMapper.actualizarEntidadDesdeDTO(alumno, dto);
        if (!alumno.getAula().getIdAula().equals(dto.getAulaId())) {
            alumno.setAula(cursoService.obtenerEntidadPorId(dto.getAulaId()));
        }
        return alumnoMapper.toDTO(alumnoRepository.save(alumno));
    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno = obtenerActivoPorId(id);
        alumno.setEliminado(true); // baja logica
        alumnoRepository.save(alumno);
    }

    @Override
    @Transactional(readOnly = true)
    public Alumno obtenerEntidadPorId(Long id) {
        return obtenerActivoPorId(id);
    }

    private Alumno obtenerActivoPorId(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado, id: " + id));
        if (alumno.isEliminado()) {
            throw new ResourceNotFoundException("El alumno con id " + id + " fue dado de baja");
        }
        return alumno;
    }
}
