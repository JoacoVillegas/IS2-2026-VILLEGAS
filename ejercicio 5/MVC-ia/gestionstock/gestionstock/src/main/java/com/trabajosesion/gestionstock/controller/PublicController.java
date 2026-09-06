package com.trabajosesion.gestionstock.controller;

import com.trabajosesion.gestionstock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER - Pagina principal PUBLICA (sin login), tal como la muestra
 * el prototipo "Pagina Principal (venta de productos)". Reutiliza
 * ProductoService.listarActivos() (mismo Service que usa la pantalla de
 * Inventario del empleado) para mostrar los productos reales cargados en
 * MySQL, pero sin exponer ninguna accion de edicion/alta/baja: esta vista
 * es de solo consulta.
 */
@Controller
public class PublicController {

    private final ProductoService productoService;

    @Autowired
    public PublicController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String paginaPrincipal(Model model) {
        model.addAttribute("productos", productoService.listarActivos());
        return "public/index";
    }
}
