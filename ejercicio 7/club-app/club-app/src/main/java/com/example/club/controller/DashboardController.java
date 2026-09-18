package com.example.club.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * "/dashboard" es la unica URL de redireccion post-login configurada en
 * SecurityConfig (defaultSuccessUrl). Este Controller redirige segun el
 * rol del usuario autenticado -- analogo al MenuController del proyecto de
 * colegio.
 */
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return esAdmin ? "redirect:/admin/dashboard" : "redirect:/acceso/dashboard";
    }
}
