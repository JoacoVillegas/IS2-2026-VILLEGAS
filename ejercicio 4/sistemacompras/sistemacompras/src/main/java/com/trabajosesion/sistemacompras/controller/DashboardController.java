package com.trabajosesion.sistemacompras.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER - Pantalla principal luego del login. Es intencionalmente
 * simple: solo arma el Model con datos de la sesion (nombre, rol) para que
 * la vista muestre un menu distinto segun si es Usuario o Administrador.
 * No contiene logica de negocio: solo lectura de sesion y navegacion.
 */
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        model.addAttribute("nombreSesion", session.getAttribute("nombreSesion"));
        model.addAttribute("esAdmin", SesionUtil.esAdministrador(session));
        return "dashboard";
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }
}
