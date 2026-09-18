package com.example.club.service.impl;

import com.example.club.entity.Imagen;
import com.example.club.entity.Persona;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.repository.ImagenRepository;
import com.example.club.service.ImagenService;
import com.example.club.service.PersonaService;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Corresponde a "crearImagen()"/"editarImagen()" del diagrama original. Se
 * implementa como Service dedicado (no como metodo de la entidad Imagen)
 * porque necesita coordinar con PersonaService (Service->Service) para
 * asociar la imagen a la persona correcta.
 */
@Service
@Transactional
public class ImagenServiceImpl implements ImagenService {

    private final ImagenRepository imagenRepository;
    private final PersonaService personaService;

    public ImagenServiceImpl(ImagenRepository imagenRepository, PersonaService personaService) {
        this.imagenRepository = imagenRepository;
        this.personaService = personaService;
    }

    @Override
    public void guardarImagenDePersona(Long personaId, MultipartFile archivo) {
        Persona persona = personaService.obtenerEntidadPorId(personaId);
        try {
            Imagen imagen = persona.getImagen();
            if (imagen == null) {
                // crearImagen(): la persona todavia no tenia foto.
                imagen = Imagen.builder()
                        .nombre(archivo.getOriginalFilename())
                        .mime(archivo.getContentType())
                        .contenido(archivo.getBytes())
                        .eliminado(false)
                        .build();
                persona.setImagen(imagen); // cascade = ALL en Persona.imagen persiste la Imagen nueva
            } else {
                // editarImagen(): la persona ya tenia una foto, se reemplaza el contenido.
                imagen.setNombre(archivo.getOriginalFilename());
                imagen.setMime(archivo.getContentType());
                imagen.setContenido(archivo.getBytes());
            }
        } catch (IOException e) {
            // Se envuelve en una excepcion no chequeada: el Controller no
            // deberia tener que lidiar con IOException de bajo nivel; el
            // GlobalExceptionHandler la traduce a un mensaje amigable.
            throw new UncheckedIOException("No se pudo leer el archivo de imagen", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Imagen obtenerImagenDePersona(Long personaId) {
        Persona persona = personaService.obtenerEntidadPorId(personaId);
        if (persona.getImagen() == null) {
            throw new ResourceNotFoundException("La persona con id " + personaId + " no tiene una imagen cargada");
        }
        return persona.getImagen();
    }
}
