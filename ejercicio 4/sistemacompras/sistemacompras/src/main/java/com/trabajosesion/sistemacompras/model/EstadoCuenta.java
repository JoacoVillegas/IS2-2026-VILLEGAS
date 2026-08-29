package com.trabajosesion.sistemacompras.model;

/**
 * ENUM DEL DOMINIO (equivale al "ENUM estadoUsuario" del diagrama de clases).
 *
 * Representa los dos unicos valores posibles del estado de una cuenta
 * (Usuario o Administrador): ACTIVO o BLOQUEADO. Se persiste en la base de
 * datos como texto (ver @Enumerated(EnumType.STRING) en Persona), en lugar
 * de como numero ordinal, para que la columna en MySQL sea legible
 * directamente ("ACTIVO" / "BLOQUEADO") y no dependa del orden en que estos
 * valores fueron declarados aca.
 */
public enum EstadoCuenta {
    ACTIVO,
    BLOQUEADO
}
