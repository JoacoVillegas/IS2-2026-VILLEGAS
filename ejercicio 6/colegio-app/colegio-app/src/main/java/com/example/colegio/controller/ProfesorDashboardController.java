package com.example.colegio.controller;

import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.service.ProfesorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfesorDashboardController {

    private final ProfesorService profesorService;

    public ProfesorDashboardController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping("/profesor/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        ProfesorDTO profesor = profesorService.buscarPorEmail(authentication.getName());
        model.addAttribute("profesor", profesor);
        return "profesor/dashboard";
    }
}
