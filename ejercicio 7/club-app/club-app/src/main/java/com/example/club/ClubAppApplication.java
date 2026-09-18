package com.example.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase de arranque de la aplicacion. @SpringBootApplication combina tres
 * anotaciones:
 *   - @Configuration: esta clase puede definir Beans.
 *   - @EnableAutoConfiguration: Spring Boot configura automaticamente
 *     Tomcat embebido, Thymeleaf, JPA/Hibernate, Spring Security, etc. en
 *     base a las dependencias presentes en el pom.xml.
 *   - @ComponentScan: escanea el paquete com.example.club (y subpaquetes)
 *     en busca de @Component, @Service, @Repository, @Controller, para
 *     registrarlos como beans automaticamente.
 *
 * El metodo main() es el punto de entrada estandar de cualquier aplicacion
 * Java; SpringApplication.run() arranca el contexto de Spring y el
 * servidor web embebido.
 */
@SpringBootApplication
public class ClubAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubAppApplication.class, args);
    }
}
