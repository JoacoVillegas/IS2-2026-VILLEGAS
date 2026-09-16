package com.example.colegio.controller;

import com.example.colegio.dto.CursoDTO;
import com.example.colegio.service.CursoService;
import com.example.colegio.service.GradoService;
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

/** Gestiona la entidad Aula presentada al usuario final como "Curso" (ver Aula.java). */
@Controller
@RequestMapping("/admin/cursos")
public class AdminCursoController {

    private final CursoService cursoService;
    private final GradoService gradoService;

    public AdminCursoController(CursoService cursoService, GradoService gradoService) {
        this.cursoService = cursoService;
        this.gradoService = gradoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarActivos());
        return "admin/curso-lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("cursoDTO", new CursoDTO());
        model.addAttribute("grados", gradoService.listarActivos());
        return "admin/curso-form";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("cursoDTO", cursoService.buscarPorId(id));
        model.addAttribute("grados", gradoService.listarActivos());
        return "admin/curso-form";
    }

    @PostMapping("/nuevo")
    public String registrar(@Valid @ModelAttribute("cursoDTO") CursoDTO dto, BindingResult resultado,
                             Model model, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("grados", gradoService.listarActivos());
            return "admin/curso-form";
        }
        cursoService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Curso registrado correctamente.");
        return "redirect:/admin/cursos";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("cursoDTO") CursoDTO dto,
                              BindingResult resultado, Model model, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("grados", gradoService.listarActivos());
            return "admin/curso-form";
        }
        cursoService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Curso actualizado correctamente.");
        return "redirect:/admin/cursos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cursoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Curso dado de baja correctamente.");
        return "redirect:/admin/cursos";
    }

    /** Alta rapida de Grado desde la misma pantalla de cursos (evita una pantalla dedicada, ver Grado.java). */
    @PostMapping("/grados/nuevo")
    public String registrarGrado(@ModelAttribute("nivel") String nivel, RedirectAttributes redirectAttributes) {
        gradoService.registrar(com.example.colegio.dto.GradoDTO.builder().nivel(nivel).build());
        redirectAttributes.addFlashAttribute("mensajeExito", "Grado agregado correctamente.");
        return "redirect:/admin/cursos/nuevo";
    }
}
