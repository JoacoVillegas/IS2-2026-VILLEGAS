package com.example.club.entity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Se invoca una vez por cada transaccion que modifica una entidad
 * @Audited, justo antes de guardar la fila en revision_info. Lee el
 * usuario actualmente autenticado desde el SecurityContext de Spring
 * Security y lo deja grabado junto con la revision.
 */
public class RevisionListener implements org.hibernate.envers.RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionInfo revisionInfo = (RevisionInfo) revisionEntity;
        String email = "SISTEMA";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            email = authentication.getName();
        }
        revisionInfo.setUsuarioEmail(email);
    }
}
