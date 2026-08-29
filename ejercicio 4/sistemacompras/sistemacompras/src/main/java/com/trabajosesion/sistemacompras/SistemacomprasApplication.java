package com.trabajosesion.sistemacompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASE DE ARRANQUE DE LA APLICACION.
 *
 * La anotacion @SpringBootApplication es en realidad la combinacion de tres
 * anotaciones de Spring:
 *
 *  - @Configuration:        marca esta clase como fuente de configuracion
 *                            de beans para el contenedor de Spring.
 *  - @EnableAutoConfiguration: le dice a Spring Boot que configure
 *                            automaticamente todo lo que detecte en el
 *                            classpath (por ejemplo: como encontramos
 *                            spring-boot-starter-web, arranca un Tomcat
 *                            embebido y activa Spring MVC; como encontramos
 *                            spring-boot-starter-data-jpa + el driver de
 *                            MySQL, configura un DataSource y un
 *                            EntityManagerFactory de Hibernate).
 *  - @ComponentScan:        le indica a Spring que escanee este paquete
 *                            (com.trabajosesion.sistemacompras) y todos sus
 *                            subpaquetes en busca de clases anotadas con
 *                            @Controller, @Service, @Repository, @Component,
 *                            etc. para registrarlas como "beans" gestionados
 *                            por el contenedor de Inversion de Control.
 *
 * Este es el unico punto de entrada del programa (metodo main). A partir de
 * aca, Spring Boot arma todo el contexto de la aplicacion: crea el servidor
 * web embebido, inicializa la conexion a MySQL, registra los Controllers,
 * Services y DAOs, y deja la aplicacion escuchando peticiones HTTP.
 */
@SpringBootApplication
public class SistemacomprasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemacomprasApplication.class, args);
    }

}
