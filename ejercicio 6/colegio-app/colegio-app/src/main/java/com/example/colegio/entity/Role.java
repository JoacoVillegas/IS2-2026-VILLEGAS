package com.example.colegio.entity;

/**
 * Roles de seguridad del sistema.
 *
 * Spring Security espera, por convencion, que los roles esten prefijados con
 * "ROLE_" cuando se usan con expresiones como hasRole("ADMIN") (Spring agrega
 * el prefijo "ROLE_" automaticamente) o hasAuthority("ROLE_ADMIN") (aqui el
 * prefijo se escribe explicitamente). En este proyecto guardamos el nombre
 * "plano" (ADMIN, PROFESOR) en la base de datos y es la clase Usuario
 * (metodo getAuthorities) la que antepone "ROLE_" al construir el
 * GrantedAuthority, evitando así confusiones entre ambas convenciones.
 */
public enum Role {
    ADMIN,
    PROFESOR
}
