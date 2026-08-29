package com.trabajosesion.sistemacompras.controller;

import com.trabajosesion.sistemacompras.service.InventarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER - Consulta del historial de movimientos de Inventario
 * (funcionalidad de Administrador para auditar entradas/salidas de stock).
 */
@Controller
public class InventarioController {

    private final InventarioService inventarioService;

    @Autowired
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/inventario")
    public String listar(HttpSession session, Model model) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        model.addAttribute("movimientos", inventarioService.listarTodos());
        return "inventario/list";
    }
}
