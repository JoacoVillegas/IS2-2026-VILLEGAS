package com.example.colegio.controller;

import com.example.colegio.dto.AlumnoDTO;
import com.example.colegio.dto.NotaDTO;
import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.entity.DictadoClases;
import com.example.colegio.service.DictadoClasesService;
import com.example.colegio.service.NotaService;
import com.example.colegio.service.ProfesorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Consultas del profesor limitadas a "las materias/cursos que tiene
 * asignados" (punto 6 del enunciado). En todos los metodos se resuelve
 * PRIMERO el profesor autenticado (nunca se confia en un id recibido desde
 * el cliente) y luego se filtra usando DictadoClasesService.
 */
@Controller
@RequestMapping("/profesor")
public class ProfesorConsultaController {

    private final ProfesorService profesorService;
    private final DictadoClasesService dictadoClasesService;
    private final com.example.colegio.service.AlumnoService alumnoService;
    private final NotaService notaService;

    public ProfesorConsultaController(ProfesorService profesorService,
                                       DictadoClasesService dictadoClasesService,
                                       com.example.colegio.service.AlumnoService alumnoService,
                                       NotaService notaService) {
        this.profesorService = profesorService;
        this.dictadoClasesService = dictadoClasesService;
        this.alumnoService = alumnoService;
        this.notaService = notaService;
    }

    @GetMapping("/materias")
    public String misMaterias(Authentication authentication, Model model) {
        ProfesorDTO profesor = profesorActual(authentication);
        List<DictadoClases> asignaciones = dictadoClasesService.listarAsignacionesDeProfesor(profesor.getId());
        model.addAttribute("asignaciones", asignaciones);
        return "profesor/materias";
    }

    @GetMapping("/cursos")
    public String misCursos(Authentication authentication, Model model) {
        ProfesorDTO profesor = profesorActual(authentication);
        List<DictadoClases> asignaciones = dictadoClasesService.listarAsignacionesDeProfesor(profesor.getId());
        model.addAttribute("asignaciones", asignaciones);
        return "profesor/cursos";
    }

    @GetMapping("/alumnos")
    public String misAlumnos(Authentication authentication, Model model) {
        ProfesorDTO profesor = profesorActual(authentication);
        List<Long> aulaIds = dictadoClasesService.listarAsignacionesDeProfesor(profesor.getId()).stream()
                .map(d -> d.getAula().getIdAula()).distinct().toList();
        List<AlumnoDTO> alumnos = aulaIds.isEmpty() ? List.of() : alumnoService.listarPorAulas(aulaIds);
        model.addAttribute("alumnos", alumnos);
        return "profesor/alumnos";
    }

    @GetMapping("/notas")
    public String misNotas(Authentication authentication, Model model) {
        ProfesorDTO profesor = profesorActual(authentication);
        model.addAttribute("notas", notaService.listarVisiblesParaProfesor(profesor.getId()));
        model.addAttribute("notaDTO", new NotaDTO());
        List<Long> aulaIds = dictadoClasesService.listarAsignacionesDeProfesor(profesor.getId()).stream()
                .map(d -> d.getAula().getIdAula()).distinct().toList();
        model.addAttribute("alumnosDisponibles", aulaIds.isEmpty() ? List.of() : alumnoService.listarPorAulas(aulaIds));
        model.addAttribute("materiasDisponibles", dictadoClasesService.listarAsignacionesDeProfesor(profesor.getId())
                .stream().map(DictadoClases::getMateria).distinct().toList());
        return "profesor/notas";
    }

    @PostMapping("/notas/nueva")
    public String registrarNota(@Valid @ModelAttribute("notaDTO") NotaDTO dto, BindingResult resultado,
                                 RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Revise los datos de la nota ingresada.");
            return "redirect:/profesor/notas";
        }
        notaService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Nota registrada correctamente.");
        return "redirect:/profesor/notas";
    }

    private ProfesorDTO profesorActual(Authentication authentication) {
        return profesorService.buscarPorEmail(authentication.getName());
    }
}
