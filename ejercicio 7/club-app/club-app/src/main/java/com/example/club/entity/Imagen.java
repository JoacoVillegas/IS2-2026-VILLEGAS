package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Imagen del rostro de una Persona. El diagrama original ya define
 * explicitamente el atributo "contenido: byte[]", por lo que se respeta tal
 * cual (REQUISITO DEL DIAGRAMA, no una decision libre): se almacena como
 * BLOB directamente en MySQL mediante @Lob, en lugar de guardar un archivo
 * en disco con una referencia/ruta en la base de datos.
 *
 * Implicancias de esta decision (documentadas, no cambiadas sin
 * justificacion, tal como pide el enunciado):
 *   - Ventaja: la imagen viaja siempre junto con el registro, sin depender
 *     de que exista un archivo en un disco especifico; simplifica backups
 *     de un unico origen (la base de datos).
 *   - Desventaja: el tamaño de la base de datos crece rapido con muchas
 *     fotos; las consultas que no necesitan la imagen deben evitar traerla
 *     (por eso Persona.imagen es FetchType.LAZY).
 * Esta entidad NO se audita (ver Persona.java, @NotAudited en la relacion):
 * auditar un BLOB completo en cada cambio duplicaria el contenido binario
 * en la tabla imagen_AUD sin aportar valor historico para este ejercicio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "imagen")
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    /** Ej: "image/jpeg", "image/png". Usado para reconstruir la respuesta HTTP al servir la imagen. */
    @Column(nullable = false, length = 100)
    private String mime;

    /**
     * @Lob: le indica a JPA/Hibernate que este campo debe mapearse a un
     * tipo de columna "Large Object".
     *
     * BUG CORREGIDO: sin especificar columnDefinition, Hibernate mapea
     * @Lob sobre un byte[] a la columna generica "BLOB" de MySQL, que
     * tiene un limite de 65.535 bytes (~64 KB). Cualquier foto real
     * (una jpg de camara/celular facilmente supera eso) hacia fallar el
     * INSERT/UPDATE con un error de "Data too long for column" -- que
     * quedaba oculto detras del mensaje generico "Ocurrio un error
     * inesperado" (ver GlobalExceptionHandler). Se fuerza explicitamente
     * columnDefinition = "LONGBLOB" (limite ~4 GB), muy por encima de
     * cualquier foto de rostro razonable.
     *
     * IMPORTANTE: como el proyecto usa ddl-auto=update, Hibernate NO
     * modifica el tipo de una columna que ya existe en la base de datos
     * (update solo agrega tablas/columnas faltantes, nunca altera las
     * existentes). Si la tabla "imagen" ya se creo con la columna
     * "contenido" en tipo BLOB, hay que recrearla para que tome LONGBLOB:
     * la forma mas simple es borrar la tabla (DROP TABLE imagen;) o toda
     * la base (DROP DATABASE club_db;) y dejar que la app la vuelva a
     * crear al arrancar.
     */
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] contenido;

    @Column(nullable = false)
    private boolean eliminado = false;
}
