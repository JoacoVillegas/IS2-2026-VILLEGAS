package com.example.colegio.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * COMO SE DETERMINA EL USUARIO ACTUALMENTE AUTENTICADO: Spring MVC puede
 * inyectar directamente un parametro de tipo Authentication en cualquier
 * metodo de un Controller; Spring Security lo completa automaticamente a
 * partir del SecurityContext asociado a la sesion HTTP actual. Authentication
 * expone getName() (el username, en este caso el correo) y getAuthorities()
 * (los roles concedidos).
 */
@Controller
public class MenuController {

    /**
     * "/menu" exige, segun SecurityConfig, estar autenticado (sin importar
     * el rol). Desde aqui se redirige al dashboard especifico de cada rol.
     */
    @GetMapping("/menu")
    public String menu(Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
        return esAdmin ? "redirect:/admin/dashboard" : "redirect:/profesor/dashboard";
    }
}
