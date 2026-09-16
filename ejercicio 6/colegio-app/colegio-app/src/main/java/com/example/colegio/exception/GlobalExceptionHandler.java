package com.example.colegio.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ControllerAdvice: convierte esta clase en un manejador de excepciones
 * GLOBAL, aplicado a todos los @Controller del proyecto. En lugar de repetir
 * try/catch en cada metodo de cada Controller, se centraliza aqui la
 * traduccion de excepciones de negocio a una vista amigable de error, y se
 * evita mostrarle al usuario un stack trace tecnico.
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

    @ExceptionHandler(Exception.class)
    public String manejarGenerico(Exception ex, Model model) {
        // Ultima linea de defensa: cualquier excepcion no prevista tambien
        // se muestra de forma amigable, nunca como stack trace crudo.
        model.addAttribute("mensajeError", "Ocurrio un error inesperado. Intente nuevamente.");
        return "error-generico";
    }
}
