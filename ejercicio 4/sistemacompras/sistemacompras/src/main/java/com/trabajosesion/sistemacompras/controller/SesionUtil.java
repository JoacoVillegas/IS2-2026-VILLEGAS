package com.trabajosesion.sistemacompras.controller;

import jakarta.servlet.http.HttpSession;

/**
 * Clase de apoyo (NO es un @Component/@Bean de Spring, es una simple clase
 * utilitaria con metodos estaticos) usada por varios Controllers para
 * verificar el estado de la sesion HTTP sin repetir el mismo bloque de
 * codigo en cada metodo.
 *
 * Se ubica junto a los Controllers (y no en Service) porque la nocion de
 * "sesion HTTP" es un detalle propio de la capa web (HttpSession es una
 * clase de la API de Servlets), no una regla de negocio: el Service no
 * deberia saber nada sobre sesiones ni cookies.
 */
public final class SesionUtil {

    private SesionUtil() {
    }

    public static boolean haySesionIniciada(HttpSession session) {
        return session.getAttribute("rol") != null;
    }

    public static boolean esAdministrador(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("rol"));
    }

    public static boolean esUsuario(HttpSession session) {
        return "USUARIO".equals(session.getAttribute("rol"));
    }

    public static Long idUsuarioActual(HttpSession session) {
        return (Long) session.getAttribute("usuarioId");
    }
}
