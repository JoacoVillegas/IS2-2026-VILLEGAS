package com.example.colegio.entity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Hibernate Envers invoca a newRevision(...) exactamente una vez por cada
 * transaccion que modifica una entidad @Audited, justo antes de guardar la
 * fila correspondiente en revision_info. Aqui se aprovecha ese punto de
 * extension para leer el usuario actualmente logueado (a traves del
 * SecurityContext que Spring Security mantiene por hilo/request) y dejarlo
 * grabado junto con la revision.
 */
public class RevisionListener implements org.hibernate.envers.RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionInfo revisionInfo = (RevisionInfo) revisionEntity;

        String email = "SISTEMA";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // authentication puede ser null (proceso batch/carga inicial) o
        // "anonymousUser" cuando no hay nadie logueado; en ambos casos se
        // deja constancia de "SISTEMA" en lugar de un email real.
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            email = authentication.getName();
        }
        revisionInfo.setUsuarioEmail(email);
    }
}
