package com.trabajosesion.sistemacompras.config;

import com.trabajosesion.sistemacompras.dao.AdministradorDAO;
import com.trabajosesion.sistemacompras.model.Administrador;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * CONFIG - Clase de configuracion (no es Controller, Service ni DAO: es
 * infraestructura de arranque de la aplicacion).
 *
 * @Configuration + un metodo que devuelve un CommandLineRunner es la forma
 * estandar de Spring Boot de ejecutar codigo una unica vez, justo despues
 * de que la aplicacion termino de arrancar (con el contexto de Spring y la
 * conexion a MySQL ya listos).
 *
 * Se usa aca para "sembrar" (seed) un usuario Administrador inicial, ya
 * que el sistema no expone un formulario publico de registro de
 * administradores (a diferencia de Usuario, que si se puede autorregistrar
 * desde /registro): normalmente el primer administrador de cualquier
 * sistema se crea por fuera de la aplicacion (script, consola, o -como
 * aca- codigo de arranque), y a partir de ahi ese administrador puede
 * gestionar el resto del sistema.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner sembrarAdministradorInicial(AdministradorDAO administradorDAO) {
        return args -> {
            boolean yaExiste = administradorDAO.findByCorreo("admin@sistema.com").isPresent();
            if (!yaExiste) {
                Administrador admin = new Administrador(
                        "Admin",
                        "Sistema",
                        "00000000",
                        LocalDate.of(1990, 1, 1),
                        "admin@sistema.com",
                        "admin123",
                        "TOTAL");
                administradorDAO.save(admin);
                System.out.println("[DataInitializer] Administrador inicial creado -> correo: admin@sistema.com / password: admin123");
            }
        };
    }
}
