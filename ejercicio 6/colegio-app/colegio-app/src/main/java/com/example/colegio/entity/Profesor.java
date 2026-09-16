package com.example.colegio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * Entidad JPA que representa a un docente. Extiende (herencia Java simple,
 * no herencia JPA) de Persona, de donde obtiene id/nombre/apellido/eliminado
 * -- ver Persona.java para la justificacion de @MappedSuperclass.
 *
 * @Entity: marca la clase como una entidad JPA; Hibernate creara/mantendra
 * una tabla "profesor" con las columnas heredadas de Persona MAS las propias.
 *
 * @Audited (Hibernate Envers): cada INSERT/UPDATE/DELETE sobre esta tabla
 * queda registrado automaticamente en una tabla paralela "profesor_AUD"
 * (mismas columnas + REV + REVTYPE). Se audita Profesor porque es una de las
 * entidades de dominio "importantes" mencionadas explicitamente en el
 * enunciado (punto 12).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "profesor")
@Audited
public class Profesor extends Persona {

    @Column(length = 100)
    private String especialidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sexo sexo;

    /**
     * @Past: se valida en el DTO (ProfesorDTO), no aqui, porque las
     * anotaciones de Jakarta Validation se aplican sobre el objeto que
     * recibe el Controller desde el formulario (el DTO), no sobre la
     * entidad JPA que maneja el Repository.
     */
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    /**
     * Relacion 1 a 1 con la identidad de acceso (ver Usuario.java para la
     * justificacion de por que la autenticacion se separa en su propia
     * entidad). fetch = EAGER porque, cada vez que se trabaja con un
     * Profesor autenticado, casi siempre se necesita tambien su Usuario
     * (email, rol) en la misma operacion.
     */
    @NotAudited
    @OneToOne(fetch = FetchType.EAGER, cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}
