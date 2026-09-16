package com.example.colegio.controller;

import com.example.colegio.dto.MateriaDTO;
import com.example.colegio.service.MateriaService;
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
@RequestMapping("/admin/materias")
public class AdminMateriaController {

    private final MateriaService materiaService;

    public AdminMateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materias", materiaService.listarActivas());
        return "admin/materia-lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("materiaDTO", new MateriaDTO());
        return "admin/materia-form";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("materiaDTO", materiaService.buscarPorId(id));
        return "admin/materia-form";
    }

    @PostMapping("/nuevo")
    public String registrar(@Valid @ModelAttribute("materiaDTO") MateriaDTO dto, BindingResult resultado,
                             RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "admin/materia-form";
        }
        materiaService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Materia registrada correctamente.");
        return "redirect:/admin/materias";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("materiaDTO") MateriaDTO dto,
                              BindingResult resultado, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "admin/materia-form";
        }
        materiaService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Materia actualizada correctamente.");
        return "redirect:/admin/materias";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        materiaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Materia dada de baja correctamente.");
        return "redirect:/admin/materias";
    }
}
