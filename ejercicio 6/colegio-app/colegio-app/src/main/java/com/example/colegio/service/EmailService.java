package com.example.colegio.service;

/**
 * Servicio dedicado exclusivamente al envio de correos (requisito del
 * enunciado, seccion 10: "NO realizar directamente el envio de correo desde
 * el Controller"; el flujo correcto es ProfesorService -> EmailService).
 */
public interface EmailService {

    /** Envia (o simula, ver application.yml app.mail-habilitado) el correo de bienvenida a un profesor nuevo. */
    void enviarBienvenidaProfesor(String destinatario, String nombreCompleto);
}
