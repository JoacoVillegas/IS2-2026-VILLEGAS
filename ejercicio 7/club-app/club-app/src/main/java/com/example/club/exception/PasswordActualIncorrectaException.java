package com.example.club.exception;

public class PasswordActualIncorrectaException extends RuntimeException {
    public PasswordActualIncorrectaException() {
        super("La contraseña actual ingresada es incorrecta");
    }
}
