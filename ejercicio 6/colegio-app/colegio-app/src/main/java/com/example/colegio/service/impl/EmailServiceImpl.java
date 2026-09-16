package com.example.colegio.service.impl;

import com.example.colegio.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Service: marca esta clase como un componente de la capa de servicio, para
 * que Spring la detecte durante el escaneo de componentes y la registre como
 * un bean inyectable (@Autowired / inyeccion por constructor).
 *
 * @Slf4j (Lombok): genera automaticamente un campo "log" (org.slf4j.Logger)
 * sin tener que escribirlo manualmente en cada clase.
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.remitente}")
    private String remitente;

    /**
     * Si es false, se evita el envio real (util para probar el resto del
     * flujo de registro de profesores sin tener credenciales SMTP validas
     * configuradas). Ver application.yml / app.mail-habilitado.
     */
    @Value("${app.mail-habilitado}")
    private boolean mailHabilitado;

    /**
     * Inyeccion por constructor (en lugar de @Autowired sobre el campo):
     * es la forma recomendada por el equipo de Spring porque permite que el
     * campo sea "final" (inmutable) y facilita escribir tests unitarios
     * (se puede instanciar la clase pasando un mock por constructor).
     */
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarBienvenidaProfesor(String destinatario, String nombreCompleto) {
        String asunto = "Bienvenido/a al sistema de gestion escolar";
        // IMPORTANTE: nunca se incluye la contraseña (ni en texto plano ni
        // cifrada) dentro del cuerpo del correo, por seguridad.
        String cuerpo = "Hola " + nombreCompleto + ",\n\n"
                + "Se ha creado su cuenta de docente en el sistema de gestion escolar. "
                + "Su usuario de acceso es su correo personal (" + destinatario + "). "
                + "Utilice la contraseña que le proporciono el administrador para iniciar sesion "
                + "y le recomendamos cambiarla desde su perfil una vez que ingrese.\n\n"
                + "Saludos.";

        if (!mailHabilitado) {
            // Modo simulado: se deja constancia en el log en lugar de intentar
            // una conexion SMTP real que fallaria sin credenciales configuradas.
            log.info("[MAIL SIMULADO] Para: {} | Asunto: {} | Cuerpo: {}", destinatario, asunto, cuerpo);
            return;
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
        log.info("Correo de bienvenida enviado a {}", destinatario);
    }
}
