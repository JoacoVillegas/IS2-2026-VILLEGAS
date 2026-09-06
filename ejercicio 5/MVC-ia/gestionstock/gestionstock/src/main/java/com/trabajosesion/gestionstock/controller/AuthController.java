package com.trabajosesion.gestionstock.controller;

import com.trabajosesion.gestionstock.model.Empleado;
import com.trabajosesion.gestionstock.service.EmpleadoService;
import com.trabajosesion.gestionstock.service.EmpleadoService.ResultadoLogin;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CONTROLLER (capa Controller de MVC) - Login y logout del sistema.
 *
 * Responsabilidades permitidas aca (y solo estas): recibir la peticion
 * HTTP, delegar en EmpleadoService, y decidir la vista/redireccion. Este
 * Controller NUNCA compara contrasenas ni accede a EmpleadoDAO
 * directamente: todo pasa por EmpleadoService (flujo obligatorio
 * Controller -> Service -> DAO).
 */
@Controller
public class AuthController {

    private final EmpleadoService empleadoService;

    @Autowired
    public AuthController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    /**
     * Flujo completo de login (punto 9 del enunciado):
     *   Vista Thymeleaf (auth/login.html)
     *   -> este metodo recibe correo/password por @RequestParam
     *   -> EmpleadoService.iniciarSesion(...) (Service)
     *      -> EmpleadoDAO.findByCorreo(...) (DAO / Spring Data JPA)
     *         -> Hibernate genera el SELECT contra MySQL
     *   -> el Service devuelve un ResultadoLogin (regla de negocio)
     *   -> este Controller traduce ese resultado en la decision de
     *      navegacion (redirigir al listado de productos, o volver al
     *      login con un mensaje de error).
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {
        ResultadoLogin resultado = empleadoService.iniciarSesion(correo, password);

        if (resultado == ResultadoLogin.EXITO) {
            Empleado empleado = empleadoService.buscarPorCorreo(correo).orElseThrow();
            session.setAttribute("empleadoId", empleado.getIdEmpleado());
            session.setAttribute("nombreEmpleado", empleado.getNombre());
            // Segun el prototipo, no existe una pantalla de "dashboard"
            // separada: apenas el empleado inicia sesion, se lo lleva
            // directo a la Vista de Empleado (Inventario).
            return "redirect:/productos";
        }

        String mensaje = switch (resultado) {
            case NO_EXISTE -> "No existe una cuenta de empleado con ese correo.";
            case CREDENCIALES_INVALIDAS -> "Correo o contrasena incorrectos.";
            default -> "No se pudo iniciar sesion.";
        };
        model.addAttribute("error", mensaje);
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
