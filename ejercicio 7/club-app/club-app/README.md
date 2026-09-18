# Sistema de Gestión de Club Deportivo — Java 21 + Spring Boot + Thymeleaf

## 1. Descripción y objetivo

Sistema académico de gestión para un club deportivo: registro de socios y
su grupo familiar, control de ingreso/salida con foto de rostro, gestión de
actividades con cupos, e inscripción de socios a esas actividades. Incorpora
el registro de **pago de cuotas por familia**, con tres medios de pago
(Efectivo, Transferencia, Mercado Pago — este último **únicamente como
categoría de dato**, ver sección 5).

El objetivo no es solo que el software funcione, sino demostrar: arquitectura
MVC de 3 capas, DTO, ORM/JPA/Hibernate, relaciones entre entidades, MySQL,
Thymeleaf, Spring Security, BCrypt, Hibernate Envers, testing (unitario,
carga y stress) y diseño responsive con Bootstrap.

## 2. Tecnologías

Java 21 · Spring Boot 3.2.5 · Spring MVC · Spring Data JPA / Hibernate ORM ·
MySQL 8 · Thymeleaf + thymeleaf-extras-springsecurity6 · Spring Security ·
Lombok · Hibernate Envers · Bootstrap 5 (plantilla "SB Admin") · JUnit 5 +
Mockito · Apache JMeter · Maven.

## 3. Arquitectura

```
Thymeleaf → Controller → DTO → Service → Repository → Entity → MySQL
```

Regla estricta (REQUISITO DEL ENUNCIADO) respetada en todo el proyecto: un
Service nunca accede al Repository de otra entidad. Si necesita datos de
otro dominio, llama al Service correspondiente
(`PagoService → GrupoFamiliarService → GrupoFamiliarRepository`, nunca
`PagoService → GrupoFamiliarRepository`). Las excepciones puntuales y
documentadas a esta regla (ej. `CursoServiceImpl`/`ActividadServiceImpl`
usando el repositorio de otra entidad solo para un conteo de solo lectura)
están explicadas con un comentario `NOTA DE ARQUITECTURA` en el propio
código.

### Estructura de paquetes

```
com.example.club
├── controller   (Login, Dashboard, Socio, Familia, Persona, Actividad, Pago, Acceso, Imagen, Perfil)
├── dto
├── mapper
├── entity
├── repository
├── service / service.impl
├── security     (UsuarioDetailsServiceImpl)
├── config       (SecurityConfig)
└── exception
```

## 4. Modelo de datos — resumen de decisiones

El análisis completo (inconsistencias del diagrama original, correcciones y
justificación de cada entidad nueva) fue presentado y confirmado antes de
escribir el código; se resume aquí:

- **`Persona`** usa herencia JPA `JOINED` (no `@MappedSuperclass`, a
  diferencia del proyecto de referencia) porque acá sí existen Personas
  "puras" (familiares no-socio) que necesitan tabla propia consultable.
- **`Socio`** extiende `Persona` (tabla `socio` con solo sus columnas
  propias: `fecha_alta`, `estado`).
- **`GrupoFamiliar`**: se separó la relación de titularidad
  (`Socio 1—1 GrupoFamiliar`, composición) de la de integrantes
  (`GrupoFamiliar 1—0..* Persona`), que el diagrama original superponía en
  una sola flecha.
- **`Imagen`**: BLOB (`@Lob byte[]`), tal cual el diagrama original —
  requisito del diagrama, no decisión libre.
- **`Inscripcion`**: entidad de asociación explícita entre `Socio` y
  `Actividad` (corrigiendo el error de notación del diagrama, que dibujaba
  esa relación como herencia).
- **`Pago`** (NUEVA): `GrupoFamiliar 1—0..* Pago`, sin entidad `Cuota`
  intermedia — decisión de diseño para evitar complejidad innecesaria (ver
  justificación completa en el análisis presentado antes del código).
- **`Usuario`**: desacoplado del dominio del club (staff, no socios).

## 5. Mercado Pago — alcance explícito

`MedioPago.MERCADO_PAGO` es **únicamente un valor de enum**. No existe SDK,
credenciales, webhooks, ni ninguna llamada HTTP hacia servicios de Mercado
Pago en todo el proyecto. Una integración real con el checkout de Mercado
Pago queda documentada como **EXTENSIÓN FUTURA / FUERA DE ALCANCE**.

## 6. DTO

Cada entidad expone su propio DTO (`SocioDTO`, `PersonaDTO`,
`GrupoFamiliarDTO`, `ActividadDTO`, `InscripcionDTO`, `PagoDTO`,
`RegistroAccesoDTO`). Los Controllers trabajan exclusivamente con DTOs; la
conversión Entity↔DTO se hace en la capa `mapper/`, invocada desde los
`*ServiceImpl`. Ver el comentario extenso en `PersonaDTO.java` para la
explicación completa de qué es un DTO y por qué se usa.

## 7. Seguridad

- Login por correo (`UsuarioDetailsServiceImpl` + `SecurityConfig`).
- Contraseñas con `BCryptPasswordEncoder` — nunca texto plano.
- Roles: **ADMIN** (gestión completa) y **RECEPCION** (solo registrar
  entradas/salidas en `/acceso/**`) — ver justificación en el análisis de
  diseño, sección 5.
- Rutas: `/login` público; `/admin/**` solo `ROLE_ADMIN`; `/acceso/**`
  `ROLE_ADMIN` o `ROLE_RECEPCION`; el resto requiere estar autenticado.

## 8. Auditoría (Hibernate Envers)

| Entidad | ¿Auditada? | Motivo |
|---|---|---|
| Persona, Socio, GrupoFamiliar, Actividad, Pago | Sí | datos de negocio relevantes; `Pago` es crítico (trazabilidad contable) |
| Imagen | No | el BLOB duplicaría contenido binario completo en `imagen_AUD` sin valor histórico |
| Inscripcion, RegistroAcceso, Usuario | No | tablas operativas/de alto volumen o datos sensibles (hash de contraseña) — ver justificación completa en cada entidad |

Se usa una `RevisionInfo` personalizada (+ `RevisionListener`) que agrega
`usuario_email` a cada revisión, para poder responder "quién y cuándo"
modificó cualquier entidad auditada.

## 9. Responsive design y CSS

La plantilla **SB Admin** (Bootstrap 5) se mantiene como base visual
(navbar, sidebar, cards, tablas, formularios). Todo el CSS propio vive en
`static/css/club.css`, separado de `styles.css`, con clases prefijadas
`.club-*` para no pisar ninguna clase global de Bootstrap. Las tablas usan
`.table-responsive`; los formularios usan el grid `row`/`col-md-*` de
Bootstrap para reorganizarse en pantallas chicas.

## 10. Configurar MySQL y variables de entorno

```sql
CREATE DATABASE IF NOT EXISTS club_db; -- opcional: la app la crea sola
```

| Variable | Default (dev) |
|---|---|
| `DB_HOST` / `DB_PORT` / `DB_NAME` | localhost / 3306 / club_db |
| `DB_USERNAME` / `DB_PASSWORD` | root / root |

## 11. Cómo ejecutar el proyecto

Requisitos: **JDK 21**, Maven 3.9+, MySQL 8.

```bash
cd club-app
mvn spring-boot:run
```

La app queda en `http://localhost:8080/login`.

## 12. Usuarios y datos de prueba

| Rol | Correo | Contraseña |
|---|---|---|
| ADMIN | admin@club.com | admin123 |
| RECEPCION | recepcion@club.com | recepcion123 |

`data.sql` también crea: 2 socios (cada uno con su grupo familiar
autogenerado), 3 personas familiares, 2 actividades con inscripciones, 3
pagos (uno por cada medio de pago) y 2 registros de acceso (uno de ellos
sin salida, para probar el flujo de "registrar salida" apenas se levanta la
app). **Credenciales solo para desarrollo local** — cambiarlas antes de
cualquier uso real.

## 13. Cómo ejecutar las pruebas unitarias

```bash
mvn test
```

`PagoServiceImplTest` cubre los 8 casos pedidos (registrar pago correcto,
importe inválido, familia inexistente, los 3 medios de pago incluyendo
Mercado Pago como categoría, consultar pagos de una familia y familia sin
pagos). `SocioServiceImplTest` verifica la creación automática del grupo
familiar al dar de alta un socio.

## 14. Cómo usar JMeter (carga y stress)

1. Abrir Apache JMeter → **File → Open** → `jmeter/club-app-plan.jmx`.
2. Verificar/editar el **HTTP Request Defaults** (Server Name: `localhost`,
   Port: `8080`) si la app corre en otro lugar.
3. Editar el **Thread Group** para cambiar usuarios/ramp-up (ver
   comentarios dentro del propio `.jmx`).
4. Ejecutar con la app ya levantada (`mvn spring-boot:run` en otra
   terminal).

**Load test** (carga esperada): valores por defecto del `.jmx` — 20
usuarios, ramp-up 10s, 5 iteraciones.

**Stress test** (carga creciente): correr el mismo plan varias veces
aumentando `Number of Threads` en cada corrida: 10 → 25 → 50 → 100 usuarios
(ajustar según el hardware disponible), registrando el **Summary Report**
de cada etapa por separado.

Métricas a observar: Response Time, Throughput, Error %, Latencia,
saturación de recursos (CPU/memoria del proceso Java, fuera de JMeter).

**Ningún resultado de estas pruebas fue ejecutado ni se afirma en este
README** — el plan está diseñado para ejecutarse, no se inventan números.
Cualquier tabla de resultados que se agregue en el futuro debe marcarse
explícitamente como *"Resultado ilustrativo"* si no proviene de una
corrida real.

## 15. Diferencia Load Test vs. Stress Test

- **Load Test**: comprueba el comportamiento del sistema bajo una carga
  esperada o determinada (uso normal).
- **Stress Test**: observa el comportamiento cuando la carga aumenta
  progresivamente más allá de las condiciones normales, buscando el punto
  de degradación o falla.

## 16. Endpoints principales

| Método | Ruta | Rol | Descripción |
|---|---|---|---|
| GET/POST | /login | público | Login |
| GET | /dashboard | autenticado | Redirige según rol |
| GET/POST | /admin/socios/** | ADMIN | ABM Socios |
| GET/POST | /admin/familias/** | ADMIN | Detalle de familia, alta de familiares, pagos |
| GET/POST | /admin/personas/** | ADMIN | Listado/detalle, carga de imagen |
| GET/POST | /admin/actividades/** | ADMIN | ABM Actividades, inscripciones |
| GET/POST | /admin/pagos/** | ADMIN | Registrar/anular pagos |
| GET/POST | /acceso/** | ADMIN o RECEPCION | Registrar entrada/salida |
| GET/POST | /cambiar-password | autenticado | Cambio de contraseña |

## 17. Objetivo académico y decisiones de diseño

Cada decisión no dictada literalmente por el enunciado está marcada en el
propio código como `DECISION DE DISENO`, `NOTA DE ARQUITECTURA` o
`CORRECCION DE INCONSISTENCIA DEL DIAGRAMA`, siguiendo la clasificación
acordada antes de generar el código (REQUISITO DEL ENUNCIADO / DECISIÓN DE
DISEÑO / EXTENSIÓN FUTURA). Revisar especialmente los comentarios de
`Persona.java`, `GrupoFamiliar.java`, `Inscripcion.java`, `Pago.java` y
`PagoServiceImpl.java` para el detalle completo.
