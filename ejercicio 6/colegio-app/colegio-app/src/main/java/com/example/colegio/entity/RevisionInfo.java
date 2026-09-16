package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

/**
 * ENTIDAD DE REVISION PERSONALIZADA DE HIBERNATE ENVERS.
 *
 * Por defecto, Envers crea automaticamente una tabla "REVINFO" con solo dos
 * columnas: REV (numero de revision, autoincremental) y REVTSTMP (timestamp
 * en milisegundos). Para poder auditar TAMBIEN "quien" hizo el cambio (no
 * solo "cuando"), se define una entidad de revision personalizada que
 * extiende DefaultRevisionEntity (hereda REV y REVTSTMP) y le agrega el
 * campo "usuarioEmail".
 *
 * @RevisionEntity(RevisionListener.class): le dice a Envers "usa esta clase
 * en lugar de la tabla REVINFO por defecto, y usa RevisionListener.class
 * para completar sus campos personalizados en cada commit".
 *
 * Funcionamiento completo:
 *   1. Cada vez que se guarda/modifica/elimina una entidad @Audited dentro
 *      de una transaccion, Hibernate Envers crea (si no existe aun para esa
 *      transaccion) una fila en la tabla "revision_info" con un nuevo
 *      numero de REV.
 *   2. Antes de persistir esa fila, invoca a RevisionListener.newRevision(),
 *      que rellena "usuarioEmail" leyendo el usuario actualmente autenticado
 *      desde el SecurityContext de Spring Security (ver RevisionListener.java).
 *   3. Cada tabla *_AUD (por ejemplo profesor_AUD) tiene una columna REV que
 *      referencia a esta tabla revision_info, permitiendo responder "quien
 *      y cuando modifico este registro" para cualquier entidad auditada.
 */
@Getter
@Setter
@Entity
@Table(name = "revision_info")
@RevisionEntity(RevisionListener.class)
public class RevisionInfo extends DefaultRevisionEntity {

    /**
     * Email del usuario autenticado que provoco el cambio, o "SISTEMA" si el
     * cambio ocurrio sin un usuario autenticado en el contexto de seguridad
     * (por ejemplo, la carga inicial de datos de prueba).
     */
    @Column(name = "usuario_email", length = 150)
    private String usuarioEmail;
}
