package com.example.colegio.service.impl;

import com.example.colegio.entity.Aula;
import com.example.colegio.entity.DictadoClases;
import com.example.colegio.entity.Materia;
import com.example.colegio.entity.Profesor;
import com.example.colegio.repository.DictadoClasesRepository;
import com.example.colegio.service.CursoService;
import com.example.colegio.service.DictadoClasesService;
import com.example.colegio.service.MateriaService;
import com.example.colegio.service.ProfesorService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cada dependencia externa (Profesor, Materia, Aula) se resuelve a traves
 * del Service correspondiente, nunca accediendo a su Repository de forma
 * directa (ver comentario en clase DictadoClases.java).
 */
@Service
@Transactional
public class DictadoClasesServiceImpl implements DictadoClasesService {

    private final DictadoClasesRepository dictadoClasesRepository;
    private final ProfesorService profesorService;
    private final MateriaService materiaService;
    private final CursoService cursoService;

    public DictadoClasesServiceImpl(DictadoClasesRepository dictadoClasesRepository,
                                     ProfesorService profesorService,
                                     MateriaService materiaService,
                                     CursoService cursoService) {
        this.dictadoClasesRepository = dictadoClasesRepository;
        this.profesorService = profesorService;
        this.materiaService = materiaService;
        this.cursoService = cursoService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DictadoClases> listarAsignacionesDeProfesor(Long profesorId) {
        return dictadoClasesRepository.findByProfesor_Id(profesorId);
    }

    @Override
    public DictadoClases asignarProfesor(Long profesorId, Long materiaId, Long aulaId, LocalDate anioLectivo) {
        Profesor profesor = profesorService.obtenerEntidadPorId(profesorId);
        Materia materia = materiaService.obtenerEntidadPorId(materiaId);
        Aula aula = cursoService.obtenerEntidadPorId(aulaId);

        DictadoClases dictado = DictadoClases.builder()
                .profesor(profesor)
                .materia(materia)
                .aula(aula)
                .anioLectivo(anioLectivo)
                .build();
        return dictadoClasesRepository.save(dictado);
    }
}
