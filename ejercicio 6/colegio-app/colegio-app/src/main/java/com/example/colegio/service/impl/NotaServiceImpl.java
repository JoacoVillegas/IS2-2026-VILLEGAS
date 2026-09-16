package com.example.colegio.service.impl;

import com.example.colegio.dto.NotaDTO;
import com.example.colegio.entity.Alumno;
import com.example.colegio.entity.DictadoClases;
import com.example.colegio.entity.Materia;
import com.example.colegio.entity.Nota;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.mapper.NotaMapper;
import com.example.colegio.repository.NotaRepository;
import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.DictadoClasesService;
import com.example.colegio.service.MateriaService;
import com.example.colegio.service.NotaService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotaServiceImpl implements NotaService {

    private final NotaRepository notaRepository;
    private final AlumnoService alumnoService;
    private final MateriaService materiaService;
    private final DictadoClasesService dictadoClasesService;
    private final NotaMapper notaMapper;

    public NotaServiceImpl(NotaRepository notaRepository,
                            AlumnoService alumnoService,
                            MateriaService materiaService,
                            DictadoClasesService dictadoClasesService,
                            NotaMapper notaMapper) {
        this.notaRepository = notaRepository;
        this.alumnoService = alumnoService;
        this.materiaService = materiaService;
        this.dictadoClasesService = dictadoClasesService;
        this.notaMapper = notaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaDTO> listarPorAlumno(Long alumnoId) {
        return notaRepository.findByAlumno_Id(alumnoId).stream().map(notaMapper::toDTO).toList();
    }

    /**
     * Implementa el requisito de autorizacion fina del enunciado (punto 6):
     * un profesor solo puede ver las notas de los cursos que efectivamente
     * tiene asignados en DictadoClases (nunca las de todo el colegio).
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotaDTO> listarVisiblesParaProfesor(Long profesorId) {
        List<DictadoClases> asignaciones = dictadoClasesService.listarAsignacionesDeProfesor(profesorId);
        List<Long> aulaIds = asignaciones.stream().map(d -> d.getAula().getIdAula()).distinct().toList();
        if (aulaIds.isEmpty()) {
            return List.of();
        }
        return notaRepository.findByAlumno_Aula_IdAulaIn(aulaIds).stream().map(notaMapper::toDTO).toList();
    }

    @Override
    public NotaDTO registrar(NotaDTO dto) {
        Alumno alumno = alumnoService.obtenerEntidadPorId(dto.getAlumnoId());
        Materia materia = materiaService.obtenerEntidadPorId(dto.getMateriaId());
        Nota nota = Nota.builder()
                .fecha(dto.getFecha())
                .valor(dto.getValor())
                .alumno(alumno)
                .materia(materia)
                .build();
        return notaMapper.toDTO(notaRepository.save(nota));
    }

    @Override
    public void eliminar(Long id) {
        // Nota es la unica entidad del dominio donde NO se aplica baja
        // logica: a diferencia de Profesor/Alumno/Materia/Curso (que son
        // "catalogos" que se siguen referenciando aunque esten dados de
        // baja), una nota erronea cargada por error no tiene sentido
        // conservarla visible; se elimina fisicamente. Aun asi, al estar
        // @Audited, Hibernate Envers deja constancia del borrado en
        // nota_AUD con REVTYPE = DEL (ver comentario en Nota.java/README).
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nota no encontrada, id: " + id));
        notaRepository.delete(nota);
    }
}
