package com.example.colegio.exception;

/** Se lanza al intentar registrar un profesor con un correo que ya existe (columna UNIQUE). */
public class EmailYaRegistradoException extends RuntimeException {
    public EmailYaRegistradoException(String email) {
        super("Ya existe un usuario registrado con el correo: " + email);
    }
}
