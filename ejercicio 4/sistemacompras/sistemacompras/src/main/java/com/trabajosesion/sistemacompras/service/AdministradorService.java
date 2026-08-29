package com.trabajosesion.sistemacompras.service;

import com.trabajosesion.sistemacompras.model.Administrador;

import java.util.Optional;

/**
 * SERVICE - Logica de negocio relacionada con Administrador (registro y
 * autenticacion). Se separa de UsuarioService porque, aunque ambas
 * entidades heredan de Persona, son dos roles con reglas y tablas
 * independientes en este modelo (un Administrador no es un Usuario).
 */
public interface AdministradorService {

    Administrador registrar(Administrador administrador);

    UsuarioService.ResultadoLogin iniciarSesion(String correo, String password);

    Optional<Administrador> buscarPorCorreo(String correo);
}
