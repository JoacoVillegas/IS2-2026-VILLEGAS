package com.example.colegio.exception;

/** Se lanza cuando, al cambiar la contraseña, la "contraseña actual" ingresada no coincide. */
public class PasswordActualIncorrectaException extends RuntimeException {
    public PasswordActualIncorrectaException() {
        super("La contraseña actual ingresada es incorrecta");
    }
}
