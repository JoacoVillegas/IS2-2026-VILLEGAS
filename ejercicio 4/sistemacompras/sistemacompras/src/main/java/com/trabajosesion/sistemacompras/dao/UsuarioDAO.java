package com.trabajosesion.sistemacompras.dao;

import com.trabajosesion.sistemacompras.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO (capa de acceso a datos, tercera capa del patron Controller -> Service -> DAO).
 *
 * @Repository: anotacion de Spring que marca esta interfaz como componente
 * de acceso a datos. Ademas, habilita la "traduccion de excepciones": si
 * MySQL/Hibernate lanza una excepcion especifica de bajo nivel, Spring la
 * convierte en una excepcion generica de su propia jerarquia
 * (DataAccessException), para que las capas superiores (Service,
 * Controller) no necesiten conocer detalles del motor de base de datos
 * concreto que se esta usando.
 *
 * Al extender JpaRepository<Usuario, Long> esta interfaz YA tiene, sin
 * escribir una sola linea de implementacion, metodos como:
 *   save(Usuario), findById(Long), findAll(), deleteById(Long), etc.
 * Spring Data JPA genera la implementacion real en tiempo de ejecucion
 * (un proxy) que traduce cada uno de estos metodos a SQL a traves de
 * Hibernate. Esta es la materializacion concreta del "DAO" pedido por el
 * enunciado: es la UNICA clase de todo el proyecto que sabe que existe una
 * base de datos relacional detras.
 *
 * IMPORTANTE (separacion de capas): ni el Controller ni la vista Thymeleaf
 * acceden nunca directamente a UsuarioDAO. Solo UsuarioServiceImpl lo hace.
 * Esto es lo que garantiza que la logica de negocio (por ejemplo, "bloquear
 * despues de 3 intentos fallidos") viva en un unico lugar (el Service) y no
 * se duplique ni se filtre hacia el Controller.
 */
@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    /**
     * Metodo de consulta derivado: Spring Data JPA interpreta el nombre
     * del metodo ("findByCorreo") y genera automaticamente la consulta
     * "SELECT * FROM usuarios WHERE correo = ?" sin que se escriba SQL ni
     * JPQL a mano. Es la busqueda que usa el login, ya que el correo es el
     * "nombre de usuario" del sistema (segun el enunciado original).
     */
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByDocumento(String documento);
}
