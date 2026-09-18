package com.example.club.controller;

import com.example.club.dto.SocioDTO;
import com.example.club.service.SocioService;
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

/**
 * Controller = capa que recibe requests HTTP y decide que vista devolver.
 * Trabaja EXCLUSIVAMENTE con DTO (SocioDTO), nunca con la entidad Socio
 * directamente (regla de arquitectura, punto 8 del enunciado).
 */
@Controller
@RequestMapping("/admin/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("socios", socioService.listarActivos());
        return "admin/socio-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("socioDTO", new SocioDTO());
        return "admin/socio-form";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("socioDTO") SocioDTO dto, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/socio-form";
        }
        socioService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Socio registrado correctamente");
        return "redirect:/admin/socios";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("socioDTO", socioService.buscarPorId(id));
        return "admin/socio-form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("socioDTO") SocioDTO dto,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/socio-form";
        }
        socioService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Socio actualizado correctamente");
        return "redirect:/admin/socios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        socioService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Socio dado de baja correctamente");
        return "redirect:/admin/socios";
    }
}
