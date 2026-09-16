-- =====================================================================================
-- DATOS DE PRUEBA
-- -------------------------------------------------------------------------------------
-- Se ejecuta automaticamente al iniciar la aplicacion (spring.sql.init.mode=always +
-- spring.jpa.defer-datasource-initialization=true en application.yml), DESPUES de que
-- Hibernate crea las tablas (ddl-auto=update).
--
-- IMPORTANTE: los hashes de contraseña de abajo son BCrypt REALES (no texto plano):
--   admin@colegio.com     -> contraseña: admin123
--   profesor@colegio.com  -> contraseña: profesor123
-- Cambiar estas contraseñas es OBLIGATORIO antes de usar el sistema en un
-- entorno real; se documentan aqui unicamente para poder probar el login
-- localmente. Ver README.md, seccion "Usuarios iniciales".
-- =====================================================================================

INSERT IGNORE INTO colegio (id_colegio, nombre, direccion, eliminado) VALUES
    (1, 'Colegio San Martin', 'Av. Libertador 1234, Mendoza', false);

INSERT IGNORE INTO grado (id_grado, nivel, eliminado, colegio_id) VALUES
    (1, '1er grado', false, 1),
    (2, '2do grado', false, 1);

INSERT IGNORE INTO aula (id_aula, division, eliminado, grado_id) VALUES
    (1, 'A', false, 1),
    (2, 'B', false, 1),
    (3, 'A', false, 2);

INSERT IGNORE INTO materia (id_materia, nombre, eliminado) VALUES
    (1, 'Matematica', false),
    (2, 'Lengua', false),
    (3, 'Ciencias Naturales', false);

-- Usuarios de acceso (ver Usuario.java). El admin no tiene fila en "profesor".
INSERT IGNORE INTO usuario (id, email, password_hash, role, habilitado) VALUES
    (1, 'admin@colegio.com', '$2b$10$xKGKMCByTtkgrj/5B/2KNOWcR4kefECZLdtEwME9kZDmVikz/nNLe', 'ADMIN', true),
    (2, 'profesor@colegio.com', '$2b$10$ZRS27z2O0RXH4iu5gMBCfukdRt/vTJ7IOYf/fsYPzaRIHN/qHAKJ2', 'PROFESOR', true);

INSERT IGNORE INTO profesor (id, nombre, apellido, eliminado, especialidad, sexo, fecha_nacimiento, usuario_id) VALUES
    (1, 'Maria', 'Gonzalez', false, 'Matematica', 'FEMENINO', '1985-03-12', 2);

INSERT IGNORE INTO alumno (id, nombre, apellido, eliminado, fecha_nacimiento, aula_id) VALUES
    (1, 'Juan', 'Perez', false, '2016-05-20', 1),
    (2, 'Lucia', 'Fernandez', false, '2016-08-02', 1),
    (3, 'Marcos', 'Diaz', false, '2016-01-15', 2);

-- La profesora Maria (id=1) dicta Matematica (id=1) en el curso "1er grado A" (aula id=1).
INSERT IGNORE INTO dictado_clases (id_dictado, anio_lectivo, profesor_id, materia_id, aula_id) VALUES
    (1, '2026-03-01', 1, 1, 1);

INSERT IGNORE INTO nota (id_nota, fecha, valor, materia_id, alumno_id) VALUES
    (1, '2026-04-10', 8.5, 1, 1),
    (2, '2026-04-10', 7.0, 1, 2);
