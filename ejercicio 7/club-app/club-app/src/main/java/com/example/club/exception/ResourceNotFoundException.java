package com.example.club.exception;

/** Ver el mismo patron en el proyecto de colegio: excepcion propia para que
 *  GlobalExceptionHandler la capture y muestre un mensaje amigable en vez
 *  de un stack trace. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
