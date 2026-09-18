-- =====================================================================
-- DATOS DE PRUEBA
-- Se usa INSERT IGNORE (no falla si ya existen filas con esos ids, util
-- al reiniciar la app en desarrollo con ddl-auto=update).
-- IMPORTANTE: los ids se fijan explicitamente a mano porque Socio usa
-- herencia JPA JOINED sobre Persona (ver Persona.java/Socio.java): la fila
-- de "socio" debe compartir el MISMO id que su fila correspondiente en
-- "persona". Hibernate hace esto automaticamente en tiempo de ejecucion;
-- aqui, al insertar SQL crudo, hay que respetarlo manualmente.
-- =====================================================================

-- ---------- USUARIOS (login) ----------
-- Contraseñas: admin123 / recepcion123 (hasheadas con BCrypt, nunca texto plano)
INSERT IGNORE INTO usuario (id, email, password_hash, role, habilitado) VALUES
    (1, 'admin@club.com', '$2b$10$M29crLgoEEaHke7FdjQ1JuUj/ZEK0wC9xqCoXe3daq89uZK21GkOi', 'ADMIN', true),
    (2, 'recepcion@club.com', '$2b$10$O0cMLJcSSfQaTUqHT/KtSeEyyyhAjWMTmyPtPq5Jz5FjcE9NjOjdO', 'RECEPCION', true);

-- ---------- PERSONAS (tabla raiz de la herencia JOINED) ----------
-- ids 1 y 2 son los dos Socios (titulares); ids 3, 4 y 5 son familiares
-- (Persona "pura", no Socio).
INSERT IGNORE INTO persona (id, nombre, apellido, fecha_nacimiento, eliminado) VALUES
    (1, 'Juan', 'Perez', '1985-04-12', false),
    (2, 'Maria', 'Gomez', '1990-07-23', false),
    (3, 'Ana', 'Perez', '2012-02-10', false),
    (4, 'Pedro', 'Perez', '2015-11-30', false),
    (5, 'Sofia', 'Gomez', '2014-05-05', false);

-- ---------- SOCIOS (columnas propias, comparten id con "persona") ----------
INSERT IGNORE INTO socio (id, fecha_alta, estado) VALUES
    (1, '2020-01-15', 'ACTIVO'),
    (2, '2021-03-10', 'ACTIVO');

-- ---------- GRUPOS FAMILIARES (titularidad) ----------
INSERT IGNORE INTO grupo_familiar (id, socio_titular_id, eliminado) VALUES
    (1, 1, false),
    (2, 2, false);

-- ---------- INTEGRANTES: se asocian las Personas 3 y 4 a la familia 1, y la 5 a la familia 2 ----------
UPDATE persona SET grupo_familiar_id = 1 WHERE id IN (3, 4);
UPDATE persona SET grupo_familiar_id = 2 WHERE id = 5;

-- ---------- ACTIVIDADES ----------
INSERT IGNORE INTO actividad (id, nombre, horario, cupos, eliminado) VALUES
    (1, 'Natacion', '09:00:00', 20, false),
    (2, 'Futbol infantil', '18:00:00', 22, false);

-- ---------- INSCRIPCIONES ----------
INSERT IGNORE INTO inscripcion (id, fecha_inscripcion, estado, socio_id, actividad_id) VALUES
    (1, '2026-03-01', 'ACTIVA', 1, 1),
    (2, '2026-03-02', 'ACTIVA', 2, 2);

-- ---------- PAGOS (medios de pago: EFECTIVO, TRANSFERENCIA, MERCADO_PAGO) ----------
INSERT IGNORE INTO pago (id, grupo_familiar_id, fecha, periodo, importe, medio_pago, estado, comprobante) VALUES
    (1, 1, '2026-08-01', '2026-08', 15000.00, 'MERCADO_PAGO', 'REGISTRADO', 'MP-998877'),
    (2, 1, '2026-09-01', '2026-09', 15000.00, 'EFECTIVO', 'REGISTRADO', NULL),
    (3, 2, '2026-09-03', '2026-09', 15000.00, 'TRANSFERENCIA', 'REGISTRADO', 'TRX-00234');

-- ---------- REGISTROS DE ACCESO ----------
-- El de Ana (persona 3) queda SIN salida a proposito, para poder probar el
-- flujo de "registrar salida" apenas se levanta la aplicacion.
INSERT IGNORE INTO registro_acceso (id, fecha, hora_entrada, hora_salida, persona_id) VALUES
    (1, '2026-09-15', '08:30:00', '10:15:00', 1),
    (2, '2026-09-15', '09:00:00', NULL, 3);
