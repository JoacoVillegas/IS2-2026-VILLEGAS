package com.example.colegio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Configuration: le indica a Spring que esta clase declara beans mediante
 * metodos anotados con @Bean (en lugar de mediante @Component + escaneo).
 *
 * @EnableWebSecurity: activa la integracion de Spring Security con Spring
 * MVC (registra los filtros de seguridad en la cadena de filtros del
 * servlet).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * @Bean: le dice a Spring "instancia este objeto una vez y ponlo a
     * disposicion de toda la aplicacion para ser inyectado donde se
     * necesite" (aqui, en UsuarioServiceImpl y ProfesorServiceImpl).
     *
     * BCryptPasswordEncoder: implementacion del algoritmo BCrypt. Se eligio
     * BCrypt (en lugar de, por ejemplo, un simple hash SHA-256) porque:
     *   - Incorpora un "salt" aleatorio distinto en cada hash generado, por
     *     lo que dos usuarios con la MISMA contraseña tendran hashes
     *     DIFERENTES en la base de datos (evita ataques de tabla arcoiris).
     *   - Es deliberadamente costoso computacionalmente (factor de trabajo
     *     configurable), lo que hace inviable un ataque de fuerza bruta
     *     masivo incluso si la base de datos se filtrara.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * QUE ES AUTORIZACION: una vez que Spring Security sabe "quien sos"
     * (autenticacion), decide "que rutas te dejo ver" en funcion de tu rol.
     * Ese mapeo ruta -> rol requerido se define exactamente aqui, con
     * authorizeHttpRequests(...).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estaticos (CSS/JS/imagenes) y la pagina de login: acceso publico.
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/login").permitAll()
                // ROLE_ADMIN: unicamente el administrador puede entrar a /admin/**
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // ROLE_PROFESOR: unicamente los docentes pueden entrar a /profesor/**
                .requestMatchers("/profesor/**").hasRole("PROFESOR")
                // Cualquier otra ruta (incluido /menu) exige, como minimo, estar autenticado.
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Usa la vista Thymeleaf propia (login.html) en lugar de la
                // pagina de login generica que provee Spring Security por defecto.
                .loginPage("/login")
                .loginProcessingUrl("/login") // URL a la que el <form> de login.html hace POST
                .usernameParameter("email")   // el campo de login es el correo, no un "username" generico
                .passwordParameter("password")
                // Redirige segun el rol logueado: MenuController decide el dashboard final.
                .defaultSuccessUrl("/menu", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            // 401.html / vista de acceso denegado propia de la plantilla Bootstrap
            .exceptionHandling(ex -> ex.accessDeniedPage("/acceso-denegado"))
            // CSRF queda HABILITADO (comportamiento por defecto de Spring
            // Security): todos los formularios Thymeleaf de este proyecto
            // incluyen el token CSRF automaticamente mediante
            // th:action="@{...}", que es la forma recomendada de generar
            // formularios cuando se usa thymeleaf-extras-springsecurity6.
            ;
        return http.build();
    }
}
