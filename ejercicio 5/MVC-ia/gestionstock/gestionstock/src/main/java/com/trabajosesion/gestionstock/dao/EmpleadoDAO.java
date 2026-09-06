package com.trabajosesion.gestionstock.dao;

import com.trabajosesion.gestionstock.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO (capa de acceso a datos). @Repository habilita la traduccion de
 * excepciones de Spring y marca esta interfaz como componente de
 * persistencia. Extender JpaRepository<Empleado, Long> le da, sin escribir
 * implementacion, los metodos save/findById/findAll/deleteById, y Spring
 * Data JPA genera en tiempo de ejecucion (via proxy dinamico) la
 * implementacion real de "findByCorreo" a partir del nombre del metodo.
 *
 * SOLO EmpleadoServiceImpl usa este DAO: ni el Controller ni la vista lo
 * conocen, respetando el flujo Controller -> Service -> DAO.
 */
@Repository
public interface EmpleadoDAO extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByCorreo(String correo);
}
