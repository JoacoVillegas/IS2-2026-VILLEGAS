package com.example.colegio.security;

import com.example.colegio.entity.Usuario;
import com.example.colegio.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * QUE ES AUTENTICACION: el proceso de verificar "quien sos" (en este
 * sistema: ¿el correo y la contraseña ingresados corresponden a un usuario
 * real del sistema?). QUE HACE SPRING SECURITY: intercepta cada peticion
 * HTTP, y para el formulario de login en particular, delega en un
 * UserDetailsService (esta clase) para obtener los datos del usuario a
 * partir del username ingresado, y compara la contraseña usando el
 * PasswordEncoder configurado (BCrypt).
 *
 * @Service: se registra como bean; SecurityConfig lo referencia
 * implicitamente al configurar el AuthenticationManager (Spring Boot lo
 * detecta automaticamente si es el unico UserDetailsService del contexto).
 */
@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Este metodo es invocado automaticamente por Spring Security en cada
     * intento de login. "username" aqui es, por requisito del enunciado, el
     * correo personal del docente (o el correo del admin).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el correo: " + username));

        // Se antepone "ROLE_" al nombre del rol (ADMIN/PROFESOR) porque es
        // la convencion que Spring Security espera para poder usar despues
        // expresiones como hasRole("ADMIN") en SecurityConfig.
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));

        // org.springframework.security.core.userdetails.User: implementacion
        // "de fabrica" de UserDetails que provee Spring Security, suficiente
        // para este proyecto (no hace falta una clase propia).
        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .disabled(!usuario.isHabilitado())
                .authorities(authorities)
                .build();
    }
}
