package com.example.club.controller;

import com.example.club.entity.Imagen;
import com.example.club.service.ImagenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Sirve/recibe la foto de rostro de una Persona. Se implementa en su propio
 * Controller (no dentro de PersonaController) porque el contrato HTTP es
 * distinto: recibe multipart/form-data y devuelve bytes crudos con su
 * Content-Type, no HTML.
 */
@Controller
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    /** Corresponde a crearImagen()/editarImagen() del diagrama original. */
    @PostMapping("/admin/personas/{personaId}/imagen")
    public String subirImagen(@PathVariable Long personaId, @RequestParam("archivo") MultipartFile archivo,
                               RedirectAttributes redirectAttributes) {
        imagenService.guardarImagenDePersona(personaId, archivo);
        redirectAttributes.addFlashAttribute("mensajeExito", "Imagen guardada correctamente");
        return "redirect:/admin/personas/" + personaId;
    }

    @GetMapping("/admin/personas/{personaId}/imagen")
    @ResponseBody
    public ResponseEntity<byte[]> verImagen(@PathVariable Long personaId) {
        Imagen imagen = imagenService.obtenerImagenDePersona(personaId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagen.getMime()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagen.getNombre() + "\"")
                .body(imagen.getContenido());
    }
}
