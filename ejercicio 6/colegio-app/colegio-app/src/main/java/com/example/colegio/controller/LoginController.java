package com.example.colegio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Controller: marca la clase como un Controller de Spring MVC "clasico"
 * (devuelve nombres de vista Thymeleaf que se resuelven contra
 * src/main/resources/templates/, a diferencia de @RestController que
 * devolveria JSON).
 *
 * Este Controller es intencionalmente minimo: el POST real del formulario de
 * login lo procesa Spring Security de forma automatica (ver
 * SecurityConfig.formLogin().loginProcessingUrl("/login")), asi que aqui
 * solo se necesita el GET que muestra la vista.
 */
@Controller
public class LoginController {

    /** Acceso publico (ver SecurityConfig: "/login" esta en permitAll()). */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    /** Vista mostrada cuando SecurityConfig.exceptionHandling() redirige por falta de permisos (rol incorrecto). */
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "401";
    }
}
