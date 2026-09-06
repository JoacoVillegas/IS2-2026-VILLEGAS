package com.trabajosesion.gestionstock.service.impl;

import com.trabajosesion.gestionstock.dao.EmpleadoDAO;
import com.trabajosesion.gestionstock.model.Empleado;
import com.trabajosesion.gestionstock.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * SERVICE - Implementacion de la logica de negocio de Empleado
 * (autenticacion). @Service la registra como bean de la capa de negocio,
 * inyectable en los Controllers.
 *
 * Flujo de login (punto 9 y 14 del enunciado):
 *   AuthController recibe correo/password del formulario Thymeleaf
 *   -> delega ENTERAMENTE en este metodo (el Controller no compara
 *      contrasenas ni decide nada)
 *   -> este Service busca el Empleado por correo via EmpleadoDAO
 *      (Spring Data JPA/Hibernate -> SELECT en MySQL)
 *   -> compara la contrasena ingresada contra el HASH guardado usando
 *      PasswordEncoder.matches(...) (bcrypt: nunca se "desencripta" el
 *      hash, se vuelve a hashear la contrasena ingresada con el mismo
 *      salt y se comparan los resultados)
 *   -> devuelve un resultado de negocio (enum), que el Controller
 *      traduce en una decision de navegacion.
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoDAO empleadoDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoDAO empleadoDAO, PasswordEncoder passwordEncoder) {
        this.empleadoDAO = empleadoDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResultadoLogin iniciarSesion(String correo, String passwordSinHashear) {
        Optional<Empleado> posibleEmpleado = empleadoDAO.findByCorreo(correo);

        if (posibleEmpleado.isEmpty()) {
            return ResultadoLogin.NO_EXISTE;
        }

        Empleado empleado = posibleEmpleado.get();

        // passwordEncoder.matches(textoPlano, hashGuardado): bcrypt extrae
        // el salt del propio hash guardado, vuelve a aplicar el algoritmo
        // sobre "passwordSinHashear" con ese mismo salt, y compara los
        // hashes resultantes. Nunca se revierte el hash a texto plano.
        if (passwordEncoder.matches(passwordSinHashear, empleado.getPassword())) {
            return ResultadoLogin.EXITO;
        }
        return ResultadoLogin.CREDENCIALES_INVALIDAS;
    }

    @Override
    public Optional<Empleado> buscarPorCorreo(String correo) {
        return empleadoDAO.findByCorreo(correo);
    }

    @Override
    @Transactional
    public Empleado registrarSiNoExiste(String nombre, String dni, String correo, String passwordSinHashear, String cargo) {
        return empleadoDAO.findByCorreo(correo).orElseGet(() -> {
            Empleado nuevo = new Empleado(nombre, dni, correo, passwordEncoder.encode(passwordSinHashear), cargo);
            return empleadoDAO.save(nuevo);
        });
    }
}
