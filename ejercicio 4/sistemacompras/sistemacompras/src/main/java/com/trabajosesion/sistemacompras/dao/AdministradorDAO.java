package com.trabajosesion.sistemacompras.dao;

import com.trabajosesion.sistemacompras.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO de la entidad Administrador. Misma logica que UsuarioDAO: Spring Data
 * JPA genera la implementacion real a partir de la interfaz.
 */
@Repository
public interface AdministradorDAO extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByCorreo(String correo);
}
