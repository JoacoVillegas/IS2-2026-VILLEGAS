package com.trabajosesion.sistemacompras.controller;

import com.trabajosesion.sistemacompras.model.Usuario;
import com.trabajosesion.sistemacompras.service.AdministradorService;
import com.trabajosesion.sistemacompras.service.UsuarioService;
import com.trabajosesion.sistemacompras.service.UsuarioService.ResultadoLogin;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

/**
 * CONTROLLER (capa Controller de MVC, primera capa del patron de 3 capas).
 *
 * Responsabilidades de un Controller en este proyecto (y NO otras):
 *   1) Recibir la peticion HTTP (@GetMapping/@PostMapping).
 *   2) Tomar los datos de entrada (formularios, sesion).
 *   3) Delegar TODA la logica de negocio al Service correspondiente
 *      (UsuarioService / AdministradorService). El Controller nunca
 *      calcula si una cuenta debe bloquearse ni compara contrasenas: eso
 *      es responsabilidad de Persona.iniciarSesion() + UsuarioServiceImpl.
 *   4) Preparar el Model (los datos que la vista Thymeleaf va a mostrar).
 *   5) Decidir la navegacion: que vista renderizar o a que URL redirigir.
 *
 * Este Controller jamas llama a UsuarioDAO/AdministradorDAO directamente:
 * siempre pasa por la interfaz de Service, respetando el flujo obligatorio
 * Controller -> Service -> DAO.
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    @Autowired
    public AuthController(UsuarioService usuarioService, AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.administradorService = administradorService;
    }

    /**
     * GET /login
     * Simplemente devuelve el nombre logico de la vista ("auth/login").
     * Spring Boot + Thymeleaf resuelven ese nombre buscando el archivo
     * src/main/resources/templates/auth/login.html.
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    /**
     * POST /login
     * Procesa el envio del formulario de login. El flujo completo pedido
     * en el enunciado (punto 9) ocurre exactamente aca:
     *
     *   Usuario (persona real) completa el formulario de la vista Thymeleaf
     *     -> este metodo del Controller recibe correo/password por @RequestParam
     *     -> delega en UsuarioService.iniciarSesion(...)
     *         -> el Service usa UsuarioDAO (Spring Data JPA/Hibernate)
     *             -> Hibernate genera el SELECT contra MySQL
     *     -> el Service devuelve un resultado de negocio (enum ResultadoLogin)
     *     -> el Controller traduce ese resultado en una decision de
     *        navegacion (redirigir al dashboard o volver al login con error)
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {

        // Primero se intenta como Usuario (el rol mas comun del sistema).
        ResultadoLogin resultado = usuarioService.iniciarSesion(correo, password);

        if (resultado == ResultadoLogin.EXITO) {
            Usuario usuario = usuarioService.buscarPorCorreo(correo).orElseThrow();
            session.setAttribute("rol", "USUARIO");
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("nombreSesion", usuario.getNombreCompleto());
            return "redirect:/dashboard";
        }

        // Si no existe como Usuario, se intenta como Administrador (mismo
        // correo puede pertenecer a un administrador del sistema).
        if (resultado == ResultadoLogin.NO_EXISTE) {
            ResultadoLogin resultadoAdmin = administradorService.iniciarSesion(correo, password);
            if (resultadoAdmin == ResultadoLogin.EXITO) {
                var admin = administradorService.buscarPorCorreo(correo).orElseThrow();
                session.setAttribute("rol", "ADMIN");
                session.setAttribute("adminId", admin.getId());
                session.setAttribute("nombreSesion", admin.getNombreCompleto());
                return "redirect:/dashboard";
            }
            resultado = resultadoAdmin;
        }

        // Cualquier otro resultado es un error: se arma un mensaje legible
        // para mostrar en la vista mediante th:if/th:text.
        String mensajeError = switch (resultado) {
            case CUENTA_BLOQUEADA -> "La cuenta esta bloqueada por 3 intentos fallidos. Contacte a un administrador.";
            case CREDENCIALES_INVALIDAS -> "Correo o contrasena incorrectos.";
            case NO_EXISTE -> "No existe una cuenta registrada con ese correo.";
            default -> "No se pudo iniciar sesion.";
        };
        model.addAttribute("error", mensajeError);
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        // Se agrega un objeto Usuario vacio al Model para poder usar
        // th:object + th:field en el formulario (data binding automatico
        // de Thymeleaf con los atributos de la entidad).
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario,
                                    @RequestParam String fechaDeNacimiento,
                                    Model model) {

        Optional<Usuario> existente = usuarioService.buscarPorCorreo(usuario.getCorreo());
        if (existente.isPresent()) {
            model.addAttribute("error", "Ya existe una cuenta registrada con ese correo.");
            return "auth/registro";
        }

        usuario.setFechaDeNacimiento(LocalDate.parse(fechaDeNacimiento));
        usuarioService.registrar(usuario);

        return "redirect:/login?registroExitoso";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
