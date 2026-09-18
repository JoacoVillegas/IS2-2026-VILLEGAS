package com.example.club.config;

import com.example.club.security.UsuarioDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Configuration + @EnableWebSecurity: activa y personaliza la seguridad
 * web de Spring Security para toda la aplicacion.
 *
 * QUE ES AUTENTICACION: verificar QUIEN es el usuario (login: correo +
 * contraseña). QUE ES AUTORIZACION: una vez identificado, decidir QUE
 * puede hacer (rutas permitidas segun su rol). Spring Security resuelve
 * ambas cosas: la autenticacion via el formulario de login (formLogin) que
 * usa UsuarioDetailsServiceImpl para buscar al usuario y BCrypt para
 * comparar contraseñas; la autorizacion via las reglas de
 * authorizeHttpRequests de abajo, evaluadas en cada request antes de que
 * llegue al Controller.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetailsServiceImpl usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsServiceImpl usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    /**
     * BCryptPasswordEncoder: implementacion de PasswordEncoder que aplica el
     * algoritmo BCrypt (hash con "salt" aleatorio incorporado, deliberadamente
     * lento para dificultar ataques de fuerza bruta). Se expone como @Bean
     * para que este disponible via inyeccion de dependencias en cualquier
     * Service que necesite cifrar/verificar contraseñas (ver
     * UsuarioServiceImpl, SocioServiceImpl al crear el primer admin, etc.).
     * NUNCA se guarda una contraseña sin pasar por este encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) -> auth
                        // Recursos estaticos y login: accesibles sin autenticarse.
                        .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/error", "/acceso-denegado").permitAll()
                        // Solo ADMIN puede administrar socios, familias, actividades, pagos y usuarios.
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // ADMIN y RECEPCION pueden registrar entradas/salidas.
                        .requestMatchers("/acceso/**").hasAnyRole("ADMIN", "RECEPCION")
                        // Cualquier otra ruta (dashboard, perfil) requiere estar autenticado.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/acceso-denegado"))
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
