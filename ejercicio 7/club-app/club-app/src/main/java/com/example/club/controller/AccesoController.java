package com.example.club.controller;

import com.example.club.service.PersonaService;
import com.example.club.service.RegistroAccesoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Rutas bajo /acceso/**: accesibles tanto por ROLE_ADMIN como por
 * ROLE_RECEPCION (ver SecurityConfig), reflejando que el personal de
 * porteria solo necesita esta funcionalidad puntual, sin acceso al resto
 * de la administracion.
 */
@Controller
@RequestMapping("/acceso")
public class AccesoController {

    private final RegistroAccesoService registroAccesoService;
    private final PersonaService personaService;

    public AccesoController(RegistroAccesoService registroAccesoService, PersonaService personaService) {
        this.registroAccesoService = registroAccesoService;
        this.personaService = personaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("registros", registroAccesoService.listarTodos());
        model.addAttribute("personas", personaService.listarActivas());
        return "acceso-dashboard";
    }

    @PostMapping("/entrada")
    public String registrarEntrada(@RequestParam Long personaId, RedirectAttributes redirectAttributes) {
        registroAccesoService.registrarEntrada(personaId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Entrada registrada correctamente");
        return "redirect:/acceso/dashboard";
    }

    @PostMapping("/{id}/salida")
    public String registrarSalida(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        registroAccesoService.registrarSalida(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Salida registrada correctamente");
        return "redirect:/acceso/dashboard";
    }
}
