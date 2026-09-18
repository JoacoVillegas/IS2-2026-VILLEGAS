package com.example.club.service;

import com.example.club.dto.RegistroAccesoDTO;
import java.util.List;

public interface RegistroAccesoService {

    List<RegistroAccesoDTO> listarTodos();

    List<RegistroAccesoDTO> listarPorPersona(Long personaId);

    /** Corresponde a "registrarEntrada()" del diagrama original. */
    RegistroAccesoDTO registrarEntrada(Long personaId);

    /** Corresponde a "registrarSalida()" del diagrama original. */
    RegistroAccesoDTO registrarSalida(Long registroId);
}
