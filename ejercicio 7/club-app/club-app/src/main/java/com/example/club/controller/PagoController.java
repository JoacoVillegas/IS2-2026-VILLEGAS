package com.example.club.controller;

import com.example.club.dto.PagoDTO;
import com.example.club.service.GrupoFamiliarService;
import com.example.club.service.PagoService;
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

/**
 * Flujo (ver analisis de diseño, punto 7):
 * Vista Thymeleaf -> PagoController -> PagoDTO -> PagoService ->
 * GrupoFamiliarService -> GrupoFamiliarRepository -> PagoRepository -> MySQL.
 */
@Controller
@RequestMapping("/admin/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final GrupoFamiliarService grupoFamiliarService;

    public PagoController(PagoService pagoService, GrupoFamiliarService grupoFamiliarService) {
        this.pagoService = pagoService;
        this.grupoFamiliarService = grupoFamiliarService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pagos", pagoService.listarTodos());
        return "admin/pago-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(@RequestParam(required = false) Long familiaId, Model model) {
        PagoDTO dto = new PagoDTO();
        dto.setGrupoFamiliarId(familiaId);
        model.addAttribute("pagoDTO", dto);
        model.addAttribute("familias", grupoFamiliarService.listarActivos());
        model.addAttribute("medios", com.example.club.entity.MedioPago.values());
        return "admin/pago-form";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("pagoDTO") PagoDTO dto, BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("familias", grupoFamiliarService.listarActivos());
            model.addAttribute("medios", com.example.club.entity.MedioPago.values());
            return "admin/pago-form";
        }
        pagoService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Pago registrado correctamente");
        return "redirect:/admin/familias/" + dto.getGrupoFamiliarId();
    }

    @PostMapping("/{id}/anular")
    public String anular(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pagoService.anular(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Pago anulado correctamente");
        return "redirect:/admin/pagos";
    }
}
