package com.example.club.controller;

import com.example.club.dto.ActividadDTO;
import com.example.club.service.ActividadService;
import com.example.club.service.InscripcionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/actividades")
public class ActividadController {

    private final ActividadService actividadService;
    private final InscripcionService inscripcionService;
    private final SocioService socioService;

    public ActividadController(ActividadService actividadService, InscripcionService inscripcionService, SocioService socioService) {
        this.actividadService = actividadService;
        this.inscripcionService = inscripcionService;
        this.socioService = socioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("actividades", actividadService.listarActivas());
        return "admin/actividad-lista";
    }

    @GetMapping("/nueva")
    public String nuevaForm(Model model) {
        model.addAttribute("actividadDTO", new ActividadDTO());
        return "admin/actividad-form";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("actividadDTO") ActividadDTO dto, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/actividad-form";
        }
        actividadService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Actividad creada correctamente");
        return "redirect:/admin/actividades";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("actividadDTO", actividadService.buscarPorId(id));
        return "admin/actividad-form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("actividadDTO") ActividadDTO dto,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/actividad-form";
        }
        actividadService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Actividad actualizada correctamente");
        return "redirect:/admin/actividades";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        actividadService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Actividad dada de baja correctamente");
        return "redirect:/admin/actividades";
    }

    @GetMapping("/{id}/inscriptos")
    public String verInscriptos(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", actividadService.buscarPorId(id));
        model.addAttribute("inscripciones", inscripcionService.listarPorActividad(id));
        model.addAttribute("socios", socioService.listarActivos());
        return "admin/actividad-inscriptos";
    }

    @PostMapping("/{id}/inscribir")
    public String inscribir(@PathVariable Long id, @RequestParam Long socioId, RedirectAttributes redirectAttributes) {
        inscripcionService.inscribirSocio(socioId, id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Socio inscripto correctamente");
        return "redirect:/admin/actividades/" + id + "/inscriptos";
    }

    @PostMapping("/{id}/inscripciones/{inscripcionId}/cancelar")
    public String cancelarInscripcion(@PathVariable Long id, @PathVariable Long inscripcionId,
                                       RedirectAttributes redirectAttributes) {
        inscripcionService.cancelarInscripcion(inscripcionId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Inscripcion cancelada correctamente");
        return "redirect:/admin/actividades/" + id + "/inscriptos";
    }
}
