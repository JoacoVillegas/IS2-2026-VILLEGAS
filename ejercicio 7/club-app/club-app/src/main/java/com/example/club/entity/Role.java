package com.example.club.entity;

/**
 * Roles de seguridad (DECISION DE DISENO: el enunciado pide "definir roles
 * adecuados al dominio" sin especificar cuales). Se definen dos:
 *   - ADMIN: gestion completa (socios, familias, actividades, pagos, usuarios).
 *   - RECEPCION: solo puede registrar entradas/salidas y consultar, reflejando
 *     el uso real de un club (personal de porteria vs. administracion).
 * Igual que en el proyecto de colegio, se guarda el nombre "plano" (sin
 * prefijo) y es Usuario.getAuthorities() quien antepone "ROLE_".
 */
public enum Role {
    ADMIN,
    RECEPCION
}
