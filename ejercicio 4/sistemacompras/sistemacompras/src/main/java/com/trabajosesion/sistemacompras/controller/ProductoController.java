package com.trabajosesion.sistemacompras.controller;

import com.trabajosesion.sistemacompras.model.Producto;
import com.trabajosesion.sistemacompras.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * CONTROLLER - Gestion de Productos (funcionalidad exclusiva del
 * Administrador: registrarProducto/editarProducto/eliminarProducto del
 * diagrama de clases).
 *
 * Notese el patron repetido en cada metodo: se verifica la sesion
 * (SesionUtil), se extraen los parametros de la peticion HTTP, se delega
 * en ProductoService, y se decide a que vista ir. En ningun momento este
 * Controller construye una consulta SQL/JPQL, ni calcula el stock a mano:
 * eso es responsabilidad exclusiva de ProductoServiceImpl.
 */
@Controller
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/productos")
    public String listar(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }

        // Se arma un mapa {producto -> stockActual} para que la vista
        // pueda mostrar el stock sin tener que recalcularlo ella misma
        // (la vista Thymeleaf nunca debe contener logica de negocio).
        Map<Long, Integer> stockPorProducto = new HashMap<>();
        for (Producto p : productoService.listarTodos()) {
            stockPorProducto.put(p.getId(), productoService.calcularStockActual(p));
        }

        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("stockPorProducto", stockPorProducto);
        model.addAttribute("esAdmin", SesionUtil.esAdministrador(session));
        return "productos/list";
    }

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        model.addAttribute("producto", new Producto());
        model.addAttribute("modoEdicion", false);
        return "productos/form";
    }

    @PostMapping("/productos")
    public String crear(HttpSession session,
                         @RequestParam String nombre,
                         @RequestParam String descripcion,
                         @RequestParam BigDecimal precio,
                         @RequestParam(defaultValue = "0") int stockInicial) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        productoService.registrarProducto(nombre, descripcion, precio, stockInicial);
        return "redirect:/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        return productoService.buscarPorId(id)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    model.addAttribute("modoEdicion", true);
                    return "productos/form";
                })
                .orElse("redirect:/productos");
    }

    @PostMapping("/productos/editar/{id}")
    public String editar(@PathVariable Long id,
                          HttpSession session,
                          @RequestParam String nombre,
                          @RequestParam String descripcion,
                          @RequestParam BigDecimal precio) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        productoService.editarProducto(id, nombre, descripcion, precio);
        return "redirect:/productos";
    }

    @PostMapping("/productos/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (!SesionUtil.esAdministrador(session)) {
            return "redirect:/dashboard";
        }
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }
}
