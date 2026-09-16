package com.example.colegio.controller;

import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller = capa que RECIBE peticiones HTTP y devuelve una vista (nunca
 * contiene reglas de negocio: solo orquesta "leer datos del formulario ->
 * pasarlos al Service -> elegir la vista de respuesta").
 *
 * Todos los metodos de este Controller trabajan exclusivamente con
 * ProfesorDTO, nunca con la entidad Profesor (regla de arquitectura, punto 3
 * del enunciado: "Los Controllers NO deben trabajar directamente con
 * entidades JPA").
 */
@Controller
@RequestMapping("/admin/profesores")
public class AdminProfesorController {

    private final ProfesorService profesorService;

    public AdminProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("profesores", profesorService.listarActivos());
        return "admin/profesor-lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("profesorDTO", new ProfesorDTO());
        model.addAttribute("esNuevo", true);
        return "admin/profesor-form";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("profesorDTO", profesorService.buscarPorId(id));
        model.addAttribute("esNuevo", false);
        return "admin/profesor-form";
    }

    /**
     * @Valid: activa las anotaciones de Jakarta Validation declaradas en
     * ProfesorDTO (@NotBlank, @Email, etc.) sobre el objeto recibido desde
     * el formulario HTML.
     * BindingResult: DEBE declararse inmediatamente despues del parametro
     * @Valid; si la validacion encuentra errores, en lugar de lanzar una
     * excepcion, los junta aqui para que el Controller decida que hacer
     * (en este caso, volver a mostrar el formulario con los mensajes).
     */
    @PostMapping("/nuevo")
    public String registrar(@Valid @ModelAttribute("profesorDTO") ProfesorDTO dto,
                             BindingResult resultado,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (dto.getPasswordInicial() == null || dto.getPasswordInicial().isBlank()) {
            resultado.rejectValue("passwordInicial", "NotBlank", "La contraseña inicial es obligatoria");
        }
        if (resultado.hasErrors()) {
            model.addAttribute("esNuevo", true);
            return "admin/profesor-form";
        }
        profesorService.registrar(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Profesor registrado correctamente. Se envio un correo de bienvenida.");
        return "redirect:/admin/profesores";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("profesorDTO") ProfesorDTO dto,
                              BindingResult resultado,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("esNuevo", false);
            return "admin/profesor-form";
        }
        profesorService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Profesor actualizado correctamente.");
        return "redirect:/admin/profesores";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        profesorService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Profesor dado de baja correctamente.");
        return "redirect:/admin/profesores";
    }
}
