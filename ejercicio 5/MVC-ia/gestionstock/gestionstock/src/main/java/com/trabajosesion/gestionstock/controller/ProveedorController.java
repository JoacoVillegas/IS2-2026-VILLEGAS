package com.trabajosesion.gestionstock.controller;

import com.trabajosesion.gestionstock.dto.ProveedorDTO;
import com.trabajosesion.gestionstock.service.ProveedorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * CONTROLLER - AMB de Proveedores (pantalla "Proveedores" del prototipo).
 * Mismo patron exacto que ProductoController: DTO de ida y vuelta,
 * validacion con @Valid antes de tocar el Service, sin logica de negocio
 * propia.
 */
@Controller
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping("/proveedores")
    public String listar(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        model.addAttribute("proveedores", proveedorService.listarActivos());
        return "proveedores/list";
    }

    @GetMapping("/proveedores/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        model.addAttribute("proveedorDTO", new ProveedorDTO());
        model.addAttribute("modoEdicion", false);
        return "proveedores/form";
    }

    @PostMapping("/proveedores")
    public String crear(HttpSession session,
                         @Valid @ModelAttribute("proveedorDTO") ProveedorDTO proveedorDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "proveedores/form";
        }
        proveedorService.registrar(proveedorDTO);
        return "redirect:/proveedores";
    }

    @GetMapping("/proveedores/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        return proveedorService.buscarPorId(id)
                .map(dto -> {
                    model.addAttribute("proveedorDTO", dto);
                    model.addAttribute("modoEdicion", true);
                    return "proveedores/form";
                })
                .orElse("redirect:/proveedores");
    }

    @PostMapping("/proveedores/editar/{id}")
    public String editar(@PathVariable Long id,
                          HttpSession session,
                          @Valid @ModelAttribute("proveedorDTO") ProveedorDTO proveedorDTO,
                          BindingResult bindingResult,
                          Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            return "proveedores/form";
        }
        proveedorService.editar(id, proveedorDTO);
        return "redirect:/proveedores";
    }

    @PostMapping("/proveedores/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        proveedorService.eliminar(id);
        return "redirect:/proveedores";
    }
}
