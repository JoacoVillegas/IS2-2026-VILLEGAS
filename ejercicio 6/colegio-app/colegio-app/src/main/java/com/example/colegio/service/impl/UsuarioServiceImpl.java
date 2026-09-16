package com.example.colegio.service.impl;

import com.example.colegio.dto.CambioPasswordDTO;
import com.example.colegio.entity.Usuario;
import com.example.colegio.exception.PasswordActualIncorrectaException;
import com.example.colegio.exception.ResourceNotFoundException;
import com.example.colegio.repository.UsuarioRepository;
import com.example.colegio.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    /**
     * PasswordEncoder es la interfaz de Spring Security; la implementacion
     * concreta (BCryptPasswordEncoder) se registra como @Bean en
     * SecurityConfig.java. Se inyecta la interfaz, no la implementacion,
     * siguiendo el principio de inversion de dependencias.
     */
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }

    /**
     * @Transactional: agrupa las operaciones de este metodo (busqueda +
     * validacion + guardado) en una unica transaccion de base de datos. Si
     * ocurre una excepcion en el medio, Spring hace ROLLBACK automatico y
     * no queda ningun cambio a medio aplicar.
     */
    @Override
    @Transactional
    public void cambiarPassword(String emailUsuarioAutenticado, CambioPasswordDTO dto) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailUsuarioAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // BCryptPasswordEncoder.matches(textoPlano, hashAlmacenado): compara
        // la contraseña ingresada por el usuario (texto plano) contra el hash
        // guardado, SIN necesitar desencriptar el hash (BCrypt es un hash de
        // una sola via, no un cifrado reversible).
        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPasswordHash())) {
            throw new PasswordActualIncorrectaException();
        }
        if (!dto.getPasswordNueva().equals(dto.getPasswordConfirmacion())) {
            throw new IllegalArgumentException("La nueva contraseña y su confirmacion no coinciden");
        }

        // encode(): aplica BCrypt (incluye un "salt" aleatorio automaticamente)
        // y genera un nuevo hash que reemplaza al anterior. BCrypt se eligio
        // porque es lento por diseño (a proposito) y resistente a ataques de
        // fuerza bruta/rainbow tables, a diferencia de un simple MD5/SHA-256.
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }
}
