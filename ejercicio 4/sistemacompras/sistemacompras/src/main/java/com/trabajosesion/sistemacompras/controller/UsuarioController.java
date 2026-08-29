package com.trabajosesion.sistemacompras.controller;

import com.trabajosesion.sistemacompras.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * CONTROLLER - Gestion de Usuarios desde el rol Administrador.
 * Corresponde al metodo Administrador.gestionarUsuario()/desbloquearUsuario()
 * del diagrama de clases.
 */
@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public String listar(HttpSession session, Model model) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/list";
    }

    @PostMapping("/usuarios/desbloquear/{id}")
    public String desbloquear(@PathVariable Long id, HttpSession session) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        // Toda la logica ("resetear intentos, volver a ACTIVO") vive en
        // UsuarioServiceImpl.desbloquear(); el Controller solo delega.
        usuarioService.desbloquear(id);
        return "redirect:/usuarios";
    }
}
