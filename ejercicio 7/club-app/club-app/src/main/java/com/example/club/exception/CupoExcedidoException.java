package com.example.club.exception;

/** Se lanza al intentar inscribir un socio a una Actividad que ya alcanzo su cupo maximo. */
public class CupoExcedidoException extends RuntimeException {
    public CupoExcedidoException(String actividadNombre) {
        super("La actividad '" + actividadNombre + "' ya alcanzo su cupo maximo de inscriptos");
    }
}
