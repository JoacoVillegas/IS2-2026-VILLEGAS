package com.example.colegio.service;

import com.example.colegio.dto.ProfesorDTO;
import com.example.colegio.entity.Profesor;
import java.util.List;

/**
 * Los Service SON la capa donde vive la logica de negocio. Se definen como
 * interfaz + implementacion (ProfesorServiceImpl) para poder, en un futuro,
 * cambiar la implementacion (o crear un mock para tests) sin tocar los
 * Controllers que dependen de esta interfaz.
 *
 * Regla de arquitectura respetada en toda la app: los metodos reciben y
 * devuelven DTO, nunca entidades JPA (ver enunciado, seccion 3 y 4).
 */
public interface ProfesorService {

    List<ProfesorDTO> listarActivos();

    ProfesorDTO buscarPorId(Long id);

    /** Registra un profesor nuevo: crea Usuario+Profesor, aplica BCrypt y envia el correo de bienvenida. */
    ProfesorDTO registrar(ProfesorDTO dto);

    ProfesorDTO actualizar(Long id, ProfesorDTO dto);

    /** Baja logica (eliminado = true), nunca DELETE fisico. */
    void eliminar(Long id);

    ProfesorDTO buscarPorEmail(String email);

    /**
     * Devuelve la ENTIDAD (no el DTO). Uso interno exclusivo de otros
     * Services (ej. DictadoClasesServiceImpl) que necesitan la referencia
     * JPA de Profesor para armar una relacion @ManyToOne, respetando la
     * regla "Service A -> Service B -> Repository B" en lugar de acceder a
     * ProfesorRepository desde otro Service.
     */
    Profesor obtenerEntidadPorId(Long id);
}
