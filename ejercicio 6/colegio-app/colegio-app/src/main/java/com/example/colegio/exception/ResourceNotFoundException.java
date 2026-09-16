package com.example.colegio.exception;

/**
 * Excepcion personalizada: se lanza desde los Services cuando se busca una
 * entidad por id y no existe (o esta dada de baja logicamente). Se prefiere
 * una excepcion propia a dejar que se propague una NoSuchElementException
 * generica: permite al GlobalExceptionHandler (@ControllerAdvice) capturarla
 * especificamente y mostrar un mensaje amigable en Thymeleaf en lugar de un
 * stack trace.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
