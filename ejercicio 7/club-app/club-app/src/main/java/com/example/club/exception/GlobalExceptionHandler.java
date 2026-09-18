package com.example.club.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ControllerAdvice centraliza el manejo de excepciones de TODOS los
 * Controllers, evitando repetir try/catch y evitando mostrarle al usuario
 * un stack trace tecnico. En que capa se atajan estos errores: siempre
 * al final de la cadena Controller->Service, nunca dentro del Service
 * (el Service solo lanza la excepcion de negocio correspondiente).
 *
 * BUG CORREGIDO: varias excepciones de negocio ya existentes en el
 * proyecto (PasswordActualIncorrectaException, y los IllegalStateException/
 * IllegalArgumentException usados por UsuarioServiceImpl y
 * RegistroAccesoServiceImpl) NO tenian un @ExceptionHandler propio, asi
 * que caian en el handler generico de mas abajo y el usuario siempre veia
 * "Ocurrio un error inesperado" en lugar del motivo real (ej. "la
 * contraseña actual ingresada es incorrecta"). Se agregan los handlers
 * especificos que faltaban.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String manejarNoEncontrado(ResourceNotFoundException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error-generico";
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    public String manejarEmailDuplicado(EmailYaRegistradoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error-generico";
    }

    @ExceptionHandler(CupoExcedidoException.class)
    public String manejarCupoExcedido(CupoExcedidoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error-generico";
    }

    @ExceptionHandler(SocioYaInscriptoException.class)
    public String manejarSocioYaInscripto(SocioYaInscriptoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error-generico";
    }

    @ExceptionHandler(PasswordActualIncorrectaException.class)
    public String manejarPasswordIncorrecta(PasswordActualIncorrectaException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error-generico";
    }

    /**
     * Cubre las validaciones de negocio "puntuales" que se expresan con las
     * excepciones estandar de Java (ej. confirmacion de contraseña que no
     * coincide, intentar registrar una entrada duplicada). Se muestra
     * ex.getMessage() porque, a diferencia de una excepcion tecnica
     * inesperada, estas SI tienen un mensaje pensado para el usuario final.
     */
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public String manejarValidacionDeNegocio(RuntimeException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error-generico";
    }

    @ExceptionHandler(Exception.class)
    public String manejarGenerico(Exception ex, Model model) {
        model.addAttribute("mensajeError", "Ocurrio un error inesperado. Intente nuevamente.");
        return "error-generico";
    }
}
