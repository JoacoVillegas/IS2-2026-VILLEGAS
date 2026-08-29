package com.trabajosesion.sistemacompras.service.impl;

import com.trabajosesion.sistemacompras.dao.AdministradorDAO;
import com.trabajosesion.sistemacompras.model.Administrador;
import com.trabajosesion.sistemacompras.model.EstadoCuenta;
import com.trabajosesion.sistemacompras.service.AdministradorService;
import com.trabajosesion.sistemacompras.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * SERVICE - Implementacion de la logica de negocio de Administrador.
 * Reutiliza el mismo algoritmo de login con bloqueo tras 3 intentos que
 * Usuario, porque esa regla esta definida una unica vez en Persona
 * (metodos iniciarSesion/sumarIntentoFallido/bloquear), evitando duplicar
 * el algoritmo entre ambos Services.
 */
@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorDAO administradorDAO;

    @Autowired
    public AdministradorServiceImpl(AdministradorDAO administradorDAO) {
        this.administradorDAO = administradorDAO;
    }

    @Override
    @Transactional
    public Administrador registrar(Administrador administrador) {
        administrador.setEstado(EstadoCuenta.ACTIVO);
        administrador.setIntentosFallidos(0);
        return administradorDAO.save(administrador);
    }

    @Override
    @Transactional
    public UsuarioService.ResultadoLogin iniciarSesion(String correo, String password) {
        Optional<Administrador> posibleAdmin = administradorDAO.findByCorreo(correo);

        if (posibleAdmin.isEmpty()) {
            return UsuarioService.ResultadoLogin.NO_EXISTE;
        }

        Administrador admin = posibleAdmin.get();

        if (admin.getEstado() == EstadoCuenta.BLOQUEADO) {
            return UsuarioService.ResultadoLogin.CUENTA_BLOQUEADA;
        }

        boolean credencialesValidas = admin.iniciarSesion(password);

        if (credencialesValidas) {
            admin.resetearIntentos();
            administradorDAO.save(admin);
            return UsuarioService.ResultadoLogin.EXITO;
        } else {
            admin.sumarIntentoFallido();
            administradorDAO.save(admin);
            return admin.getEstado() == EstadoCuenta.BLOQUEADO
                    ? UsuarioService.ResultadoLogin.CUENTA_BLOQUEADA
                    : UsuarioService.ResultadoLogin.CREDENCIALES_INVALIDAS;
        }
    }

    @Override
    public Optional<Administrador> buscarPorCorreo(String correo) {
        return administradorDAO.findByCorreo(correo);
    }
}
