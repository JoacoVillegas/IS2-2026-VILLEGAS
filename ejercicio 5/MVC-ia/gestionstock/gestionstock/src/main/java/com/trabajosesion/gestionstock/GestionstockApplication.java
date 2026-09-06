package com.trabajosesion.gestionstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion. @SpringBootApplication combina
 * @Configuration + @EnableAutoConfiguration + @ComponentScan: Spring Boot
 * detecta automaticamente los starters del pom.xml (web, thymeleaf,
 * data-jpa) y configura Tomcat embebido, el motor de vistas y la conexion
 * a MySQL sin configuracion manual adicional; y escanea este paquete y
 * subpaquetes en busca de @Controller/@Service/@Repository/@Component.
 */
@SpringBootApplication
public class GestionstockApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestionstockApplication.class, args);
    }
}
