package com.example.club.service;

import com.example.club.dto.CambioPasswordDTO;

public interface UsuarioService {

    void cambiarPassword(String emailUsuarioAutenticado, CambioPasswordDTO dto);

    boolean existeEmail(String email);
}
