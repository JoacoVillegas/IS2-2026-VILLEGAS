package com.example.colegio.service;

import com.example.colegio.dto.CambioPasswordDTO;

public interface UsuarioService {

    /** Cambia la contraseña del usuario autenticado, validando la contraseña actual y aplicando BCrypt. */
    void cambiarPassword(String emailUsuarioAutenticado, CambioPasswordDTO dto);

    /**
     * Expuesto para que OTROS Services (ej. ProfesorServiceImpl) puedan
     * validar unicidad de correo SIN acceder directamente a
     * UsuarioRepository, respetando la regla "Service A -> Service B ->
     * Repository B" (nunca "Service A -> Repository B").
     */
    boolean existeEmail(String email);
}
