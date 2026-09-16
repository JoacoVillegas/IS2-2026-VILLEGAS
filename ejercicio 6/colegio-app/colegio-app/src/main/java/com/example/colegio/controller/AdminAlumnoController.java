package com.example.colegio.controller;

import com.example.colegio.dto.AlumnoDTO;
import com.example.colegio.service.AlumnoService;
import com.example.colegio.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/alumnos")
public class AdminAlumnoController {

    private final AlumnoService alumnoService;
    private final CursoService cursoService;

    public AdminAlumnoController(AlumnoService alumnoService, CursoService cursoService) {
        this.alumnoService = alumnoService;
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alumnos", alumnoService.listarActivos());
        return "admin/alumno-lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("alumnoDTO", new AlumnoDTO());
        model.addAttribute("cursos", cursoService.listarActivos());
        return "admin/alumno-form";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("alumnoDTO", alumnoService.buscarPorId(id));
        model.addAttribute("cursos", cursoService.listarActivos());
        return "admin/alumno-form";
    }

    @PostMapping("/nuevo")
    public String registrar(@Valid @ModelAttribute("alumnoDTO") AlumnoDTO dto, BindingResult resultado,
                             Model model, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("cursos", cursoService.listarActivos());
            return "admin/alumno-form";
        }
        alumnoService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno registrado correctamente.");
        return "redirect:/admin/alumnos";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("alumnoDTO") AlumnoDTO dto,
                              BindingResult resultado, Model model, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("cursos", cursoService.listarActivos());
            return "admin/alumno-form";
        }
        alumnoService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno actualizado correctamente.");
        return "redirect:/admin/alumnos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alumnoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno dado de baja correctamente.");
        return "redirect:/admin/alumnos";
    }
}
