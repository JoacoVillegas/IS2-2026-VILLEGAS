package com.example.club.exception;

public class EmailYaRegistradoException extends RuntimeException {
    public EmailYaRegistradoException(String email) {
        super("Ya existe un usuario registrado con el correo: " + email);
    }
}
