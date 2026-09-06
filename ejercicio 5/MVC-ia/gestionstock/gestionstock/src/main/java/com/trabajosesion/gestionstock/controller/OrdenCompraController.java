package com.trabajosesion.gestionstock.controller;

import com.trabajosesion.gestionstock.dao.EmpleadoDAO;
import com.trabajosesion.gestionstock.dto.ItemOrdenDTO;
import com.trabajosesion.gestionstock.dto.NuevaOrdenDTO;
import com.trabajosesion.gestionstock.model.Empleado;
import com.trabajosesion.gestionstock.service.OrdenCompraService;
import com.trabajosesion.gestionstock.service.ProductoService;
import com.trabajosesion.gestionstock.service.ProveedorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER - Pantalla "Realizar compra" del prototipo: registrar una
 * orden de compra a un proveedor y consultar el historial de ordenes ya
 * emitidas.
 *
 * NOTA: al igual que en el ejercicio anterior, se usa EmpleadoDAO
 * directamente aca (unica excepcion al principio "Controller nunca
 * accede al DAO") solo para reconstruir la entidad Empleado completa a
 * partir del id primitivo guardado en la sesion HTTP; esa entidad se le
 * pasa entera a OrdenCompraService.registrarOrden(...), que es quien
 * hace todo el trabajo de negocio real.
 */
@Controller
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final EmpleadoDAO empleadoDAO;

    @Autowired
    public OrdenCompraController(OrdenCompraService ordenCompraService, ProveedorService proveedorService,
                                  ProductoService productoService, EmpleadoDAO empleadoDAO) {
        this.ordenCompraService = ordenCompraService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.empleadoDAO = empleadoDAO;
    }

    @GetMapping("/ordenes/nueva")
    public String mostrarFormulario(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
        return "ordenes/nueva";
    }

    /**
     * Arma el DTO de entrada NuevaOrdenDTO a partir de los tres arrays
     * paralelos que envia el formulario Thymeleaf (mismo indice = misma
     * fila de la tabla de productos), y recien ENTONCES delega en el
     * Service. Esta es la unica parte del proyecto que "traduce" el
     * formato bruto de un formulario HTML a un DTO de negocio.
     */
    @PostMapping("/ordenes")
    public String registrar(HttpSession session,
                             @RequestParam Long idProveedor,
                             @RequestParam List<Long> productoId,
                             @RequestParam List<Integer> cantidad,
                             @RequestParam List<BigDecimal> precioUnitario) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }

        Empleado empleado = empleadoDAO.findById(SesionUtil.idEmpleadoActual(session)).orElseThrow();

        NuevaOrdenDTO nuevaOrden = new NuevaOrdenDTO();
        nuevaOrden.setIdProveedor(idProveedor);
        List<ItemOrdenDTO> items = new ArrayList<>();
        for (int i = 0; i < productoId.size(); i++) {
            items.add(new ItemOrdenDTO(productoId.get(i), cantidad.get(i), precioUnitario.get(i)));
        }
        nuevaOrden.setItems(items);

        ordenCompraService.registrarOrden(empleado, nuevaOrden);
        return "redirect:/ordenes";
    }

    @GetMapping("/ordenes")
    public String historial(HttpSession session, Model model) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        model.addAttribute("ordenes", ordenCompraService.listarHistorial());
        return "ordenes/list";
    }

    @PostMapping("/ordenes/anular/{id}")
    public String anular(@PathVariable Long id, HttpSession session) {
        if (!SesionUtil.haySesionIniciada(session)) {
            return "redirect:/login";
        }
        ordenCompraService.anularOrden(id);
        return "redirect:/ordenes";
    }
}
