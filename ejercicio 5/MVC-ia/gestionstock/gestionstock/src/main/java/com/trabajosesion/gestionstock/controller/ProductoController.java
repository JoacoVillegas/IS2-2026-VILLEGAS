package com.trabajosesion.gestionstock.controller;

import com.trabajosesion.gestionstock.dto.ProductoDTO;
import com.trabajosesion.gestionstock.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * CONTROLLER (capa Controller de MVC) - AMB de Productos (pantalla
 * "Inventario" del prototipo). Es el ejemplo mas completo del flujo con
 * DTO pedido por el enunciado:
 *
 *   ALTA:
 *     GET  /productos/nuevo   -> arma un ProductoDTO vacio para el formulario
 *     POST /productos         -> recibe ProductoDTO (@Valid), delega en
 *                                 ProductoService.registrar(dto) y ese
 *                                 Service devuelve OTRO ProductoDTO ya con
 *                                 el id asignado por la base.
 *
 *   MODIFICACION:
 *     GET  /productos/editar/{id} -> ProductoService.buscarPorId(id)
 *                                     devuelve un ProductoDTO para precargar
 *                                     el formulario (NUNCA la entidad Producto).
 *     POST /productos/editar/{id} -> recibe el ProductoDTO modificado.
 *
 *   BAJA (logica):
 *     POST /productos/eliminar/{id} -> ProductoService.eliminar(id).
 *
 *   LISTADO:
 *     GET /productos -> ProductoService.listarActivos() devuelve
 *                        List<ProductoDTO>, que Thymeleaf recorre con
 *                        th:each directamente (nunca se itera una lista
 *                        de entidades Producto en la vista).
 *
 * Este Controller NO calcula el stock, no decide si un producto puede
 * eliminarse, ni arma consultas: todo eso es responsabilidad exclusiva de
 * ProductoServiceImpl.
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
        model.addAttribute("productos", productoService.listarActivos());
        return "productos/list";
    }

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        model.addAttribute("productoDTO", new ProductoDTO());
        model.addAttribute("modoEdicion", false);
        return "productos/form";
    }

    @PostMapping("/productos")
    public String crear(HttpSession session,
                         @Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        // Si @Valid detecto errores (por ejemplo, nombre en blanco o
        // precio negativo), se vuelve a mostrar el formulario con los
        // mensajes de error, SIN llegar a tocar el Service ni la base.
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "productos/form";
        }
        productoService.registrar(productoDTO);
        return "redirect:/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        return productoService.buscarPorId(id)
                .map(dto -> {
                    model.addAttribute("productoDTO", dto);
                    model.addAttribute("modoEdicion", true);
                    return "productos/form";
                })
                .orElse("redirect:/productos");
    }

    @PostMapping("/productos/editar/{id}")
    public String editar(@PathVariable Long id,
                          HttpSession session,
                          @Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO,
                          BindingResult bindingResult,
                          Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            return "productos/form";
        }
        productoService.editar(id, productoDTO);
        return "redirect:/productos";
    }

    @PostMapping("/productos/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}
