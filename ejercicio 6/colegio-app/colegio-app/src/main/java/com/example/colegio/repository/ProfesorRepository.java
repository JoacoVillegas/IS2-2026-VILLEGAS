package com.example.colegio.repository;

import com.example.colegio.entity.Profesor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Los Repository son la UNICA capa que conversa directamente con Spring Data
 * JPA / Hibernate. Extender JpaRepository<Profesor, Long> le da, gratis,
 * metodos como save(), findById(), findAll(), deleteById(), sin escribir
 * ninguna implementacion (Spring genera la implementacion en tiempo de
 * ejecucion mediante un proxy).
 *
 * Los metodos "findByXxx" son "query methods": Spring Data JPA interpreta el
 * nombre del metodo y genera automaticamente la consulta SQL/JPQL
 * correspondiente.
 */
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    List<Profesor> findByEliminadoFalse();

    Optional<Profesor> findByUsuario_Email(String email);
}
