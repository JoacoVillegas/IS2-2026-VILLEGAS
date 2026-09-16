package com.example.colegio.controller;

import com.example.colegio.dto.CambioPasswordDTO;
import com.example.colegio.dto.PerfilProfesorDTO;
import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.service.ProfesorService;
import com.example.colegio.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfesorPerfilController {

    private final ProfesorService profesorService;
    private final UsuarioService usuarioService;

    public ProfesorPerfilController(ProfesorService profesorService, UsuarioService usuarioService) {
        this.profesorService = profesorService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/profesor/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        ProfesorDTO profesor = profesorService.buscarPorEmail(authentication.getName());
        PerfilProfesorDTO perfil = PerfilProfesorDTO.builder()
                .nombre(profesor.getNombre())
                .apellido(profesor.getApellido())
                .especialidad(profesor.getEspecialidad())
                .correo(profesor.getCorreo())
                .fechaNacimiento(profesor.getFechaNacimiento())
                .build();
        model.addAttribute("perfil", perfil);
        return "profesor/perfil";
    }

    @GetMapping("/profesor/cambiar-password")
    public String formularioCambiarPassword(Model model) {
        model.addAttribute("cambioPasswordDTO", new CambioPasswordDTO());
        return "profesor/cambiar-password";
    }

    /**
     * El cambio de contraseña "debe requerir que el usuario este
     * autenticado" (punto 8 del enunciado): se cumple automaticamente
     * porque esta ruta cae bajo /profesor/**, protegida por SecurityConfig.
     * Ademas se identifica SIEMPRE al usuario a traves de la sesion
     * (authentication.getName()), nunca a partir de un id recibido del
     * formulario, para que un profesor jamas pueda cambiar la contraseña
     * de otro.
     */
    @PostMapping("/profesor/cambiar-password")
    public String cambiarPassword(Authentication authentication,
                                   @Valid @ModelAttribute("cambioPasswordDTO") CambioPasswordDTO dto,
                                   BindingResult resultado,
                                   RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "profesor/cambiar-password";
        }
        usuarioService.cambiarPassword(authentication.getName(), dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Contraseña actualizada correctamente.");
        return "redirect:/profesor/perfil";
    }
}
