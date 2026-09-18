package com.example.club.controller;

import com.example.club.dto.PersonaDTO;
import com.example.club.service.GrupoFamiliarService;
import com.example.club.service.PagoService;
import com.example.club.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * "Familia" es el nombre que usa el enunciado para lo que el diagrama llama
 * "GrupoFamiliar" (ver analisis de diseño). Se mantiene el nombre de
 * entidad/servicio original (GrupoFamiliar) por trazabilidad con el
 * diagrama, pero las URLs y vistas usan "familia", que es el termino que
 * pide el enunciado.
 */
@Controller
@RequestMapping("/admin/familias")
public class FamiliaController {

    private final GrupoFamiliarService grupoFamiliarService;
    private final PersonaService personaService;
    private final PagoService pagoService;

    public FamiliaController(GrupoFamiliarService grupoFamiliarService, PersonaService personaService, PagoService pagoService) {
        this.grupoFamiliarService = grupoFamiliarService;
        this.personaService = personaService;
        this.pagoService = pagoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("familias", grupoFamiliarService.listarActivos());
        return "admin/familia-lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("familia", grupoFamiliarService.buscarPorId(id));
        model.addAttribute("pagos", pagoService.listarPorFamilia(id));
        model.addAttribute("personasDisponibles", personaService.listarActivas());
        model.addAttribute("personaDTO", new PersonaDTO());
        return "admin/familia-detalle";
    }

    /** Alta de una Persona nueva directamente como integrante de esta familia. */
    @PostMapping("/{id}/familiares/nuevo")
    public String agregarFamiliarNuevo(@PathVariable Long id, @Valid @ModelAttribute("personaDTO") PersonaDTO dto,
                                        BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Revise los datos del familiar");
            return "redirect:/admin/familias/" + id;
        }
        PersonaDTO creada = personaService.registrar(dto);
        grupoFamiliarService.agregarFamiliar(id, creada.getId());
        redirectAttributes.addFlashAttribute("mensajeExito", "Familiar agregado correctamente");
        return "redirect:/admin/familias/" + id;
    }

    /** Asocia una Persona YA existente (que aun no pertenece a ningun grupo) como integrante. */
    @PostMapping("/{id}/familiares/existente")
    public String agregarFamiliarExistente(@PathVariable Long id, @RequestParam Long personaId,
                                            RedirectAttributes redirectAttributes) {
        grupoFamiliarService.agregarFamiliar(id, personaId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Familiar asociado correctamente");
        return "redirect:/admin/familias/" + id;
    }
}
