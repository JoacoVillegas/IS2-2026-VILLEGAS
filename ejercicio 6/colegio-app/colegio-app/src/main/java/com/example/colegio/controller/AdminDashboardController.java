package com.example.colegio.controller;

import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.CursoService;
import com.example.colegio.service.MateriaService;
import com.example.colegio.service.ProfesorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashboard del ADMIN (protegido por SecurityConfig via /admin/**, requiere
 * ROLE_ADMIN). Muestra contadores simples reutilizando los mismos Services
 * usados por los CRUD, sin logica de negocio propia en el Controller
 * (regla: "no poner logica de negocio importante dentro de Controllers").
 */
@Controller
public class AdminDashboardController {

    private final ProfesorService profesorService;
    private final AlumnoService alumnoService;
    private final MateriaService materiaService;
    private final CursoService cursoService;

    public AdminDashboardController(ProfesorService profesorService, AlumnoService alumnoService,
                                     MateriaService materiaService, CursoService cursoService) {
        this.profesorService = profesorService;
        this.alumnoService = alumnoService;
        this.materiaService = materiaService;
        this.cursoService = cursoService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("cantidadProfesores", profesorService.listarActivos().size());
        model.addAttribute("cantidadAlumnos", alumnoService.listarActivos().size());
        model.addAttribute("cantidadMaterias", materiaService.listarActivas().size());
        model.addAttribute("cantidadCursos", cursoService.listarActivos().size());
        return "admin/dashboard";
    }
}
