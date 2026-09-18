package com.example.club.security;

import com.example.club.entity.Usuario;
import com.example.club.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementacion de UserDetailsService, la interfaz que Spring Security usa
 * durante el login para buscar un usuario por su "username" (aqui, el
 * correo). Se antepone "ROLE_" al nombre del rol guardado en la base de
 * datos (Usuario.role guarda "ADMIN"/"RECEPCION" sin prefijo) porque es la
 * convencion que Spring Security espera al usar hasRole("ADMIN") en
 * SecurityConfig.
 */
@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el correo: " + email));

        return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.isHabilitado(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()))
        );
    }
}
