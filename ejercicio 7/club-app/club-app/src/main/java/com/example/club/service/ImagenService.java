package com.example.club.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImagenService {

    /** Corresponde a "crearImagen()"/"editarImagen()" del diagrama: crea o reemplaza la foto de una Persona. */
    void guardarImagenDePersona(Long personaId, MultipartFile archivo);

    /** Devuelve el contenido binario + mime para poder servir la imagen por HTTP. */
    com.example.club.entity.Imagen obtenerImagenDePersona(Long personaId);
}
