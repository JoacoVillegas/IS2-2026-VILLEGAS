# Sistema de Gestion Escolar — Java + Spring Boot + Thymeleaf

Proyecto academico que implementa un sistema de gestion escolar con
arquitectura MVC de 3 capas (Controller → Service → Repository), ORM con
JPA/Hibernate sobre MySQL, DTOs para la comunicacion entre capas, Spring
Security con autenticacion basada en roles, auditoria con Hibernate Envers y
envio de correo con Spring Mail.

## 1. Descripcion del proyecto

El sistema administra un colegio: profesores, materias, cursos (aulas) y
alumnos, y permite registrar notas de los alumnos por materia. Existen dos
roles:

- **ADMIN**: administra profesores, materias, cursos y alumnos (ABM completo).
- **PROFESOR**: consulta su perfil, cambia su contraseña, y consulta/gestiona
  informacion (materias, cursos, alumnos, notas) unicamente de aquello que
  tiene asignado (ver `DictadoClases`).

## 2. Tecnologias

Java 17 · Spring Boot 3.2.5 · Spring MVC · Spring Data JPA / Hibernate ORM ·
MySQL 8 · Thymeleaf (+ thymeleaf-extras-springsecurity6) · Spring Security ·
Lombok · Hibernate Envers (auditoria) · Bootstrap 5 (plantilla "SB Admin") ·
Spring Mail · Maven.

## 3. Arquitectura

```
View (Thymeleaf) → Controller → DTO → Service → Repository → Entity → MySQL
```

Regla estricta respetada en todo el proyecto: un Service **nunca** accede al
Repository de otra entidad; si necesita datos de otro dominio, llama al
Service correspondiente (`Service A → Service B → Repository B`). Ver
comentarios en cada `*ServiceImpl` para el detalle caso por caso.

Estructura de paquetes:

```
com.example.colegio
├── controller      (Login, Menu, Admin*, Profesor*)
├── dto
├── mapper          (conversion Entity <-> DTO)
├── entity          (JPA)
├── repository      (Spring Data JPA)
├── service / service.impl
├── security        (UserDetailsService)
├── config          (SecurityConfig)
└── exception       (excepciones propias + GlobalExceptionHandler)
```

## 4. Analisis del diagrama de clases provisto y decisiones de diseño

Se partio del diagrama de clases entregado (Colegio, Grado, Aula, Persona,
Profesor, Alumno, Materia, DictadoClases, Nota) y se lo respeto como
referencia principal. Cambios y decisiones sobre ese diseño original:

1. **Inconsistencia corregida**: en el diagrama, `Nota` solo se relaciona con
   `Materia`. Se agrego la relacion `Nota → Alumno`, indispensable porque el
   enunciado pide "notas por materia **de los alumnos**".
2. **Persona** se implementa como `@MappedSuperclass` (no como jerarquia JPA
   con tabla propia): ver justificacion en `Persona.java`.
3. **Usuario** (nueva entidad, no estaba en el diagrama) separa la
   autenticacion (correo/contraseña/rol) de los datos academicos del
   Profesor. Ver `Usuario.java`.
4. **"Curso"** (pedido por el enunciado nuevo) se implementa administrando la
   entidad **Aula** del diagrama original (Grado = nivel, Aula = division
   dentro de ese nivel = lo que coloquialmente es "un curso"). Ver
   `Aula.java` / `CursoDTO.java`.
5. **DictadoClases** se interpreta como la asignacion "un profesor dicta una
   materia en un curso durante un año lectivo", y es la base de la
   autorizacion fina del profesor (solo ve lo que tiene asignado).
6. **Colegio** se mantiene del diagrama pero sin ABM propio (no se pidio
   explicitamente); se siembra un unico registro via `data.sql`.

Cada una de estas decisiones esta ademas comentada en el codigo fuente, en
la clase correspondiente, bajo el rotulo "DECISION DE DISENO ADICIONAL" o
"INCONSISTENCIA DETECTADA", tal como pide el enunciado.

## 5. Configurar MySQL

```sql
CREATE DATABASE IF NOT EXISTS colegio_db;
```

(No es estrictamente necesario crearla a mano: la cadena de conexion incluye
`createDatabaseIfNotExist=true`.)

## 6. Variables de entorno

| Variable | Descripcion | Valor por defecto (dev) |
|---|---|---|
| `DB_HOST` / `DB_PORT` / `DB_NAME` | Conexion a MySQL | localhost / 3306 / colegio_db |
| `DB_USERNAME` / `DB_PASSWORD` | Credenciales MySQL | root / root |
| `MAIL_HOST` / `MAIL_PORT` | Servidor SMTP | smtp.gmail.com / 587 |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Credenciales SMTP | (vacias) |
| `MAIL_FROM` | Remitente de los correos | no-reply@colegio.com |
| `MAIL_HABILITADO` | `true` para enviar correos reales, `false` para modo simulado (log) | false |

Ejemplo (Linux/Mac):
```bash
export DB_USERNAME=root
export DB_PASSWORD=miClave
export MAIL_HABILITADO=false
```
En Windows (PowerShell): `$env:DB_PASSWORD="miClave"`.

## 7. Configurar SMTP (envio de correo)

Con Gmail:
1. Activar verificacion en 2 pasos en la cuenta de Google.
2. Generar una "Contraseña de aplicacion" (Google Account → Seguridad).
3. Configurar `MAIL_USERNAME` (tu correo) y `MAIL_PASSWORD` (la contraseña
   de aplicacion generada, NO tu contraseña normal) y `MAIL_HABILITADO=true`.

Si no se dispone de credenciales SMTP, dejar `MAIL_HABILITADO=false`
(valor por defecto): `EmailServiceImpl` registrara el correo en el log de la
aplicacion (`[MAIL SIMULADO] ...`) en lugar de intentar el envio real, para
poder probar todo el resto del flujo de registro de profesores.

## 8. Como ejecutar el proyecto

Requisitos: **JDK 21** (ver nota abajo), Maven 3.9+, MySQL 8 corriendo en
`localhost:3306` (o las variables de entorno configuradas apuntando a tu
servidor).

> **Nota sobre la version de Java**: el enunciado del ejercicio pide Java 17
> salvo razon tecnica justificada. Se uso Java 21 (LTS) porque es el JDK
> instalado en el entorno de desarrollo real donde se probo el proyecto.
> Spring Boot 3.2.5 soporta Java 21 sin cambios de dependencias; ver la
> propiedad `java.version` en `pom.xml`. Si se dispone de JDK 17, alcanza
> con cambiar esa propiedad a `17` y volver a compilar.

```bash
cd colegio-app
mvn spring-boot:run
```

La aplicacion queda disponible en `http://localhost:8080/login`.

Alternativa desde IntelliJ IDEA / VS Code: importar como proyecto Maven y
ejecutar `ColegioAppApplication.main()`.

## 9. Usuarios iniciales (datos de prueba, `data.sql`)

| Rol | Correo | Contraseña |
|---|---|---|
| ADMIN | admin@colegio.com | admin123 |
| PROFESOR | profesor@colegio.com | profesor123 |

**Estas credenciales son SOLO para desarrollo local.** Cambiarlas (o eliminar
esos registros) antes de cualquier uso real. Las contraseñas se guardan
siempre cifradas con BCrypt, nunca en texto plano.

`data.sql` tambien crea: un Colegio, 2 Grados, 3 Aulas/Cursos, 3 Materias,
un profesor de ejemplo con una materia asignada, 3 alumnos y 2 notas, para
poder probar el sistema de punta a punta apenas arranca.

## 10. Roles y seguridad

- `/login` → publico.
- `/menu` → requiere estar autenticado; redirige a `/admin/dashboard` o
  `/profesor/dashboard` segun el rol.
- `/admin/**` → exige `ROLE_ADMIN`.
- `/profesor/**` → exige `ROLE_PROFESOR`.

Las contraseñas se almacenan con `BCryptPasswordEncoder`. El login usa el
correo como username (`usernameParameter("email")` en `SecurityConfig`).

## 11. Auditoria (Hibernate Envers)

Entidades auditadas (`@Audited`): `Profesor`, `Alumno`, `Materia`, `Aula`
(Curso), `Nota`, `Colegio`, `Grado`. **No** se audita `Usuario`
(evita duplicar hashes de contraseña en tablas historicas) ni
`DictadoClases` (tabla de asignacion/planificacion, fuera del alcance de
auditoria pedido).

Por cada entidad auditada, Hibernate Envers crea automaticamente una tabla
`<tabla>_AUD` (por ejemplo `profesor_AUD`) con las mismas columnas mas:
- `REV`: numero de revision (referencia a la tabla `revision_info`).
- `REVTYPE`: tipo de cambio (`0` = ADD, `1` = MOD, `2` = DEL).

Se definio una entidad de revision personalizada (`RevisionInfo` +
`RevisionListener`, ver `entity/RevisionInfo.java`) que ademas de
`REV`/`REVTSTMP` (fecha) agrega `usuario_email`: el correo del usuario
autenticado que provoco el cambio, permitiendo responder "quien y cuando
modifico este registro" para cualquier entidad auditada.

Que sucede al modificar/eliminar una entidad auditada: cada
`UPDATE`/`DELETE` genera una fila nueva en la tabla `_AUD` correspondiente
(con `REVTYPE` `1` o `2`), preservando el estado anterior; los datos nunca
se pierden, solo se marcan de baja logicamente (excepto `Nota`, ver punto
13 mas abajo).

## 12. Principales endpoints

| Metodo | Ruta | Rol | Descripcion |
|---|---|---|---|
| GET/POST | /login | publico | Login |
| POST | /logout | autenticado | Logout |
| GET | /menu | autenticado | Redirige segun rol |
| GET/POST | /admin/profesores, /admin/profesores/nuevo, /admin/profesores/{id}/editar, /admin/profesores/{id}/eliminar | ADMIN | ABM Profesores |
| GET/POST | /admin/materias/** | ADMIN | ABM Materias |
| GET/POST | /admin/cursos/** | ADMIN | ABM Cursos (Aula) |
| GET/POST | /admin/alumnos/** | ADMIN | ABM Alumnos |
| GET | /profesor/dashboard, /perfil, /materias, /cursos, /alumnos, /notas | PROFESOR | Consultas propias |
| GET/POST | /profesor/cambiar-password | PROFESOR | Cambio de contraseña |
| POST | /profesor/notas/nueva | PROFESOR | Carga de nota |

## 13. Decisiones de diseño adicionales (resumen)

- **Baja logica** en Profesor/Alumno/Materia/Curso/Grado/Colegio (campo
  `eliminado`), para no romper referencias historicas ni de auditoria. La
  unica excepcion es **Nota**: una nota cargada por error se elimina
  fisicamente (no tiene sentido conservarla visible como "de baja"), aunque
  Envers de todos modos deja constancia del borrado en `nota_AUD`.
- El correo del profesor (username) es **inmutable** una vez creado el
  usuario, para simplificar el ejercicio y no romper la sesion activa.
- `AulaRepository`/`AlumnoRepository` se combinan puntualmente en
  `CursoServiceImpl` solo para el conteo de alumnos por curso (ver
  comentario en esa clase).

## 14. Como probar el sistema (testing manual)

1. **Login correcto**: ingresar con `admin@colegio.com` / `admin123` → debe
   redirigir a `/admin/dashboard`.
2. **Login incorrecto**: contraseña erronea → mensaje "Correo o contraseña
   incorrectos" en `/login?error=true`.
3. **Acceso de profesor**: ingresar con `profesor@colegio.com` /
   `profesor123` → redirige a `/profesor/dashboard`.
4. **Profesor intenta acceder a /admin**: estando logueado como profesor,
   navegar a `/admin/profesores` → redirige a `/acceso-denegado` (401).
5. **Acceso de administrador**: verificar que el admin puede entrar a todas
   las pantallas de `/admin/**`.
6. **Registro de profesor**: Admin → Profesores → Nuevo profesor, completar
   el formulario → verificar que aparece en el listado y que, con
   `MAIL_HABILITADO=false`, el log muestra `[MAIL SIMULADO] ...`.
7. **Envio de correo real**: configurar credenciales SMTP validas y
   `MAIL_HABILITADO=true`, repetir el paso anterior y verificar la
   recepcion del correo de bienvenida.
8. **Cambio de contraseña**: como profesor, ir a "Cambiar contraseña",
   probar con la contraseña actual incorrecta (debe fallar) y luego con la
   correcta (debe permitir cambiarla; volver a loguearse con la nueva).
9. **Alta de alumno / materia / curso**: repetir el flujo de alta en cada
   ABM del admin y verificar que aparecen en los listados.
10. **Registro de nota**: como profesor, ir a "Notas", cargar una nota para
    un alumno de un curso propio, verificar que aparece en la tabla.
11. **Auditoria de una modificacion**: editar un profesor y luego consultar
    la tabla `profesor_AUD` (por ejemplo con un cliente MySQL) — debe
    aparecer una fila nueva con `REVTYPE=1` y el `usuario_email` del admin
    que hizo el cambio (via `revision_info`).
12. **Auditoria de una eliminacion**: dar de baja un alumno y verificar en
    `alumno_AUD` una fila con `REVTYPE=1` (baja logica = UPDATE) o, en el
    caso puntual de una Nota eliminada fisicamente, una fila en `nota_AUD`
    con `REVTYPE=2`.

## 15. Objetivo academico

Este proyecto prioriza la claridad arquitectonica sobre la brevedad: cada
clase incluye comentarios explicando el "que" y el "por que" (no solo
comentarios obvios), especialmente en los puntos donde se tomo una decision
tecnica no dictada literalmente por el enunciado. Revisar los comentarios de
`Persona.java`, `Usuario.java`, `Aula.java`, `Nota.java`, `DictadoClases.java`
y `RevisionInfo.java` para el detalle de cada decision de diseño.
