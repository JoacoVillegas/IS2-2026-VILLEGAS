package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

/**
 * Entidad de revision personalizada de Hibernate Envers. Igual patron que en
 * el proyecto de colegio: extiende DefaultRevisionEntity (hereda REV y
 * REVTSTMP) y agrega "usuarioEmail" para poder responder "quien" hizo cada
 * cambio, ademas de "cuando".
 */
@Getter
@Setter
@Entity
@Table(name = "revision_info")
@RevisionEntity(RevisionListener.class)
public class RevisionInfo extends DefaultRevisionEntity {

    @Column(name = "usuario_email", length = 150)
    private String usuarioEmail;
}
