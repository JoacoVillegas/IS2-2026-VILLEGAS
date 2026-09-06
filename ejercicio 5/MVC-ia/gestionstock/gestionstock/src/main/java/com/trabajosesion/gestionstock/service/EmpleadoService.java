package com.trabajosesion.gestionstock.service;

import com.trabajosesion.gestionstock.model.Empleado;

import java.util.Optional;

/**
 * SERVICE (capa de logica de negocio) - contrato de operaciones sobre
 * Empleado. El Controller depende de esta interfaz, no de la
 * implementacion concreta (inversion de dependencias): permite reemplazar
 * la logica interna sin tocar los Controllers.
 */
public interface EmpleadoService {

    enum ResultadoLogin { EXITO, CREDENCIALES_INVALIDAS, NO_EXISTE }

    ResultadoLogin iniciarSesion(String correo, String passwordSinHashear);

    Optional<Empleado> buscarPorCorreo(String correo);

    Empleado registrarSiNoExiste(String nombre, String dni, String correo, String passwordSinHashear, String cargo);
}
