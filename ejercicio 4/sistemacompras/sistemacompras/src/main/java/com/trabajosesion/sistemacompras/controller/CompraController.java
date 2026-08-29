package com.trabajosesion.sistemacompras.controller;

import com.trabajosesion.sistemacompras.dao.UsuarioDAO;
import com.trabajosesion.sistemacompras.model.Usuario;
import com.trabajosesion.sistemacompras.service.CompraService;
import com.trabajosesion.sistemacompras.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CONTROLLER - Registro de compras y consulta del historial (funcionalidad
 * exclusiva del rol Usuario, tal como esta modelado en el diagrama:
 * Usuario "1" --- "0..*" Compra).
 *
 * NOTA sobre UsuarioDAO en un Controller: aca se hace una excepcion
 * deliberada y MINIMA al principio "Controller nunca accede al DAO", solo
 * para poder recuperar la entidad Usuario completa a partir del id
 * numerico guardado en la sesion HTTP (la sesion solo guarda el id, un
 * dato primitivo, no el objeto completo). Esta lectura puntual por clave
 * primaria no es logica de negocio: es preparar el argumento que despues
 * SI se delega enteramente a CompraService.registrarCompra(). Si se
 * quisiera evitar incluso esto, se podria agregar un metodo
 * UsuarioService.buscarPorId(id); se deja documentado aca como una
 * decision de diseño consciente, no como un descuido.
 */
@Controller
public class CompraController {

    private final CompraService compraService;
    private final ProductoService productoService;
    private final UsuarioDAO usuarioDAO;

    @Autowired
    public CompraController(CompraService compraService, ProductoService productoService, UsuarioDAO usuarioDAO) {
        this.compraService = compraService;
        this.productoService = productoService;
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/compras/nueva")
    public String mostrarFormulario(HttpSession session, Model model) {
        if (!SesionUtil.esUsuario(session)) {
            return "redirect:/dashboard";
        }
        model.addAttribute("productos", productoService.listarTodos());
        return "compras/nueva";
    }

    @PostMapping("/compras")
    public String registrarCompra(HttpSession session,
                                   @RequestParam List<Long> productoId,
                                   @RequestParam List<Integer> cantidad,
                                   Model model) {
        if (!SesionUtil.esUsuario(session)) {
            return "redirect:/dashboard";
        }

        Usuario usuario = usuarioDAO.findById(SesionUtil.idUsuarioActual(session))
                .orElseThrow();

        // El formulario Thymeleaf envia dos listas paralelas (mismo indice
        // = mismo producto): se combinan aca en el mapa que espera el
        // Service. Este es el unico lugar del proyecto que "traduce" el
        // formato de un formulario HTML a un tipo de dato de negocio; el
        // Service ya no sabe nada sobre como se armo ese mapa.
        Map<Long, Integer> cantidadesPorProducto = new LinkedHashMap<>();
        for (int i = 0; i < productoId.size(); i++) {
            cantidadesPorProducto.put(productoId.get(i), cantidad.get(i));
        }

        compraService.registrarCompra(usuario, cantidadesPorProducto);
        return "redirect:/compras";
    }

    @GetMapping("/compras")
    public String historial(HttpSession session, Model model) {
        if (!SesionUtil.esUsuario(session)) {
            return "redirect:/dashboard";
        }
        Usuario usuario = usuarioDAO.findById(SesionUtil.idUsuarioActual(session))
                .orElseThrow();
        model.addAttribute("compras", compraService.historialDeCompras(usuario));
        return "compras/historial";
    }
}
