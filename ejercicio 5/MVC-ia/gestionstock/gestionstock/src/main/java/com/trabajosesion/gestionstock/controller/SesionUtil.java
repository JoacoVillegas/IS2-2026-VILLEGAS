package com.trabajosesion.gestionstock.controller;

import jakarta.servlet.http.HttpSession;

/**
 * Utilidad de sesion HTTP compartida por los Controllers (no es un bean
 * de Spring, es una clase de apoyo con metodos estaticos). Se ubica junto
 * a los Controllers porque HttpSession es un concepto de la capa web,
 * ajeno a la logica de negocio del Service.
 */
public final class SesionUtil {

    private SesionUtil() {
    }

    public static boolean haySesionIniciada(HttpSession session) {
        return session.getAttribute("empleadoId") != null;
    }

    public static Long idEmpleadoActual(HttpSession session) {
        return (Long) session.getAttribute("empleadoId");
    }
}
