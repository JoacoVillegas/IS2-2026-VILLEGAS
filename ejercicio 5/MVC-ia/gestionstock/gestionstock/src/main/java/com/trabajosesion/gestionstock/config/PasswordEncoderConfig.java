package com.trabajosesion.gestionstock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * CONFIG - Registra un bean de tipo PasswordEncoder para toda la
 * aplicacion.
 *
 * BCryptPasswordEncoder implementa el algoritmo bcrypt: un algoritmo de
 * hashing pensado especificamente para contrasenas (a diferencia de
 * MD5/SHA que son rapidos y por eso MALOS para este uso, bcrypt es
 * deliberadamente lento e incluye un "salt" aleatorio distinto en cada
 * hash, lo que evita que dos contrasenas iguales generen el mismo hash y
 * dificulta mucho los ataques de fuerza bruta).
 *
 * Se expone como @Bean (en lugar de instanciarlo con "new" dentro de cada
 * Service que lo necesite) para poder inyectarlo por constructor en
 * EmpleadoServiceImpl, siguiendo el mismo mecanismo de Inversion de
 * Control que se usa para inyectar los DAO.
 *
 * IMPORTANTE: esto es lo unico que se usa de "Spring Security" en este
 * proyecto (la dependencia agregada es spring-security-crypto, no el
 * starter completo). No hay filtros de seguridad, ni SecurityFilterChain,
 * ni formularios de login propios del framework: el control de acceso
 * (quien puede ver que pagina) se sigue haciendo a mano con HttpSession
 * en los Controllers, igual que en el ejercicio anterior. Se opto por
 * esto para cumplir el pedido explicito del enunciado de "no usar una
 * arquitectura de seguridad mas compleja de la necesaria".
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
