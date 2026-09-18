package com.example.club.service.impl;

import com.example.club.dto.CambioPasswordDTO;
import com.example.club.entity.Usuario;
import com.example.club.exception.PasswordActualIncorrectaException;
import com.example.club.exception.ResourceNotFoundException;
import com.example.club.repository.UsuarioRepository;
import com.example.club.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
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
     * @Transactional: agrupa busqueda+validacion+guardado en una unica
     * transaccion; si la validacion de contraseña falla a mitad de camino,
     * no debe quedar nada escrito a medias.
     */
    @Override
    @Transactional
    public void cambiarPassword(String emailUsuarioAutenticado, CambioPasswordDTO dto) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailUsuarioAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPasswordHash())) {
            throw new PasswordActualIncorrectaException();
        }
        if (!dto.getPasswordNueva().equals(dto.getPasswordConfirmacion())) {
            throw new IllegalArgumentException("La nueva contraseña y su confirmacion no coinciden");
        }
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }
}
