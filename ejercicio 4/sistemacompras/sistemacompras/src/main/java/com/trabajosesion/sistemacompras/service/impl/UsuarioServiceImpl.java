package com.trabajosesion.sistemacompras.service.impl;

import com.trabajosesion.sistemacompras.dao.UsuarioDAO;
import com.trabajosesion.sistemacompras.model.EstadoCuenta;
import com.trabajosesion.sistemacompras.model.Usuario;
import com.trabajosesion.sistemacompras.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * SERVICE - Implementacion concreta de la logica de negocio de Usuario.
 *
 * @Service: anotacion de Spring que marca esta clase como un "bean" de la
 * capa de logica de negocio. Permite que el Controller la reciba
 * automaticamente por inyeccion de dependencias (constructor/@Autowired),
 * sin que el Controller tenga que hacer "new UsuarioServiceImpl()" el
 * mismo (esto es Inversion de Control: el contenedor de Spring crea y
 * "inyecta" el objeto, no lo crea a mano quien lo usa).
 *
 * @Transactional: envuelve el metodo en una transaccion de base de datos.
 * Si algo falla a mitad de camino (por ejemplo, una excepcion inesperada),
 * Spring hace ROLLBACK de todos los cambios hechos hasta ese punto, para
 * que la base de datos nunca quede en un estado a medio actualizar.
 *
 * ESTA CLASE ES LA UNICA QUE CONOCE A UsuarioDAO: el Controller
 * (UsuarioController / AuthController) SOLO conoce esta interfaz de
 * Service, nunca el DAO. Asi se cumple el flujo obligatorio
 * Controller -> Service -> DAO.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    @Transactional
    public Usuario registrar(Usuario usuario) {
        // Regla de negocio: un usuario nuevo siempre arranca ACTIVO y sin
        // intentos fallidos, sin importar lo que haya llegado desde el
        // formulario (evita que alguien intente registrar una cuenta ya
        // bloqueada manipulando el formulario).
        usuario.setEstado(EstadoCuenta.ACTIVO);
        usuario.setIntentosFallidos(0);
        // El DAO es quien efectivamente dialoga con Hibernate/MySQL.
        return usuarioDAO.save(usuario);
    }

    @Override
    @Transactional
    public ResultadoLogin iniciarSesion(String correo, String password) {
        Optional<Usuario> posibleUsuario = usuarioDAO.findByCorreo(correo);

        if (posibleUsuario.isEmpty()) {
            return ResultadoLogin.NO_EXISTE;
        }

        Usuario usuario = posibleUsuario.get();

        if (usuario.getEstado() == EstadoCuenta.BLOQUEADO) {
            return ResultadoLogin.CUENTA_BLOQUEADA;
        }

        // La regla "la contrasena es correcta y la cuenta no esta
        // bloqueada" vive en el Model (Persona.iniciarSesion), pero quien
        // decide QUE HACER con el resultado (sumar intento fallido,
        // bloquear, guardar en la base) es este Service.
        boolean credencialesValidas = usuario.iniciarSesion(password);

        if (credencialesValidas) {
            usuario.resetearIntentos();
            usuarioDAO.save(usuario);
            return ResultadoLogin.EXITO;
        } else {
            usuario.sumarIntentoFallido();
            usuarioDAO.save(usuario);
            return usuario.getEstado() == EstadoCuenta.BLOQUEADO
                    ? ResultadoLogin.CUENTA_BLOQUEADA
                    : ResultadoLogin.CREDENCIALES_INVALIDAS;
        }
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioDAO.findByCorreo(correo);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioDAO.findAll();
    }

    @Override
    @Transactional
    public void desbloquear(Long idUsuario) {
        // Regla de negocio propia del Administrador
        // (Administrador.desbloquearUsuario() en el diagrama de clases).
        usuarioDAO.findById(idUsuario).ifPresent(usuario -> {
            usuario.resetearIntentos();
            usuarioDAO.save(usuario);
        });
    }
}
