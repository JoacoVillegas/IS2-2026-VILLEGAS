package com.example.club.controller;

import com.example.club.dto.CambioPasswordDTO;
import com.example.club.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Disponible para cualquier usuario autenticado (ADMIN o RECEPCION), no solo para /admin/**. */
@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/cambiar-password")
    public String form(Model model) {
        model.addAttribute("cambioPasswordDTO", new CambioPasswordDTO());
        return "cambiar-password";
    }

    @PostMapping("/cambiar-password")
    public String cambiar(@Valid @ModelAttribute("cambioPasswordDTO") CambioPasswordDTO dto, BindingResult result,
                           Authentication authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cambiar-password";
        }
        usuarioService.cambiarPassword(authentication.getName(), dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Contraseña actualizada correctamente");
        return "redirect:/dashboard";
    }
}
