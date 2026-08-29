package com.trabajosesion.sistemacompras.dao;

import com.trabajosesion.sistemacompras.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO de la entidad Producto. Responsable exclusivamente de las
 * operaciones CRUD contra la tabla "productos" mediante Hibernate/JPA.
 * No contiene ninguna regla de negocio (por ejemplo, no decide si un
 * producto puede eliminarse o no; eso lo resuelve ProductoServiceImpl).
 */
@Repository
public interface ProductoDAO extends JpaRepository<Producto, Long> {
}
