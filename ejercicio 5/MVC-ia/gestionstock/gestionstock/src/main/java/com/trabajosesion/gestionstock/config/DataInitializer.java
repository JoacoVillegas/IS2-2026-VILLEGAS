package com.trabajosesion.gestionstock.config;

import com.trabajosesion.gestionstock.service.EmpleadoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CONFIG - Siembra un Empleado inicial al arrancar la aplicacion, para
 * poder iniciar sesion la primera vez sin necesidad de insertar datos a
 * mano en MySQL. Igual que en el ejercicio anterior: el prototipo no
 * contempla una pantalla publica de "registro de empleados" (los
 * empleados se dan de alta por otro empleado ya autenticado, o -como
 * aca- se crea el primero por codigo de arranque).
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner sembrarEmpleadoInicial(EmpleadoService empleadoService) {
        return args -> {
            empleadoService.registrarSiNoExiste(
                    "Empleado Demo",
                    "00000000",
                    "empleado@tienda.com",
                    "empleado123",
                    "Encargado de Deposito");
            System.out.println("[DataInitializer] Empleado inicial disponible -> correo: empleado@tienda.com / password: empleado123");
        };
    }
}
