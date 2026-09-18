package com.example.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DECISION DE DISENO: a diferencia del proyecto de colegio (donde el
 * Profesor SI necesitaba loguearse), aqui Usuario queda DESACOPLADO de
 * Socio/Persona. Quienes usan este sistema son empleados del club
 * (administracion/recepcion), no cada socio individualmente -- el
 * enunciado no pide un portal de autogestion para socios. Por eso Usuario
 * es una entidad de acceso independiente, sin relacion con el dominio del
 * club.
 *
 * No se audita (igual criterio que en el proyecto de colegio): no tiene
 * sentido versionar el hash de la contraseña en una tabla historica.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Hash BCrypt, nunca texto plano. */
    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean habilitado = true;
}
