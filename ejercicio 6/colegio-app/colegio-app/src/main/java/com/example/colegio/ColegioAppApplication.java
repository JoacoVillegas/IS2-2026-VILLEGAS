package com.example.colegio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * @SpringBootApplication es una anotacion "combinada" que agrupa:
 *   - @Configuration: esta clase puede declarar beans.
 *   - @EnableAutoConfiguration: Spring Boot configura automaticamente (segun
 *     las dependencias presentes en el pom.xml) Tomcat embebido, Thymeleaf,
 *     el DataSource de MySQL, Hibernate/JPA, Spring Security, Spring Mail, etc.
 *   - @ComponentScan: escanea el paquete com.example.colegio y sus
 *     subpaquetes en busca de @Component/@Service/@Repository/@Controller.
 */
@SpringBootApplication
public class ColegioAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColegioAppApplication.class, args);
    }
}
