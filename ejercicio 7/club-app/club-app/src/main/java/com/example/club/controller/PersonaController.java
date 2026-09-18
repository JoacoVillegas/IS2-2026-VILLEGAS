package com.example.club.controller;

import com.example.club.service.PersonaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("personas", personaService.listarActivas());
        return "admin/persona-lista";
    }

    /** Vista de detalle: muestra los datos y el formulario para subir/reemplazar la foto (ver ImagenController). */
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("persona", personaService.buscarPorId(id));
        return "admin/persona-detalle";
    }
}
