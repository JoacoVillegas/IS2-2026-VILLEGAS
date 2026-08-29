package com.trabajosesion.sistemacompras.service;

import com.trabajosesion.sistemacompras.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * SERVICE (capa intermedia del patron Controller -> Service -> DAO).
 *
 * Esta interfaz define QUE operaciones de negocio existen sobre Usuario,
 * sin decir COMO se implementan. El Controller depende de esta interfaz
 * (no de la clase concreta), lo que permite cambiar la implementacion en
 * el futuro sin tocar los Controllers (principio de inversion de
 * dependencias). La implementacion real esta en UsuarioServiceImpl.
 *
 * Aca vive la logica de negocio (por ejemplo, el algoritmo de login con
 * bloqueo tras 3 intentos), NO el acceso a datos crudo (eso es tarea del
 * DAO) ni el renderizado de HTML (eso es tarea del Controller + Thymeleaf).
 */
public interface UsuarioService {

    Usuario registrar(Usuario usuario);

    /**
     * Resultado del intento de login. Se modela como un enum simple en
     * lugar de un booleano para poder distinguir claramente entre
     * "credenciales invalidas" y "cuenta bloqueada", algo que un booleano
     * no podria expresar y que la vista necesita mostrar con mensajes
     * distintos.
     */
    enum ResultadoLogin { EXITO, CREDENCIALES_INVALIDAS, CUENTA_BLOQUEADA, NO_EXISTE }

    ResultadoLogin iniciarSesion(String correo, String password);

    Optional<Usuario> buscarPorCorreo(String correo);

    List<Usuario> listarTodos();

    void desbloquear(Long idUsuario);
}
